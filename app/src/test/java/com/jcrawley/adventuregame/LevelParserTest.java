package com.jcrawley.adventuregame;

import static org.junit.Assert.assertEquals;

import com.jcrawley.adventuregame.service.level.Choice;
import com.jcrawley.adventuregame.service.level.Level;
import com.jcrawley.adventuregame.service.level.LevelParser;
import com.jcrawley.adventuregame.service.level.Page;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class LevelParserTest {

    private LevelParser levelParser;


    @Before
    public void init(){
        levelParser = new LevelParser();
    }


    @Test
    public void canParseInput() {
        levelParser.initLevel();

        String input = """
                1
                Here is the beginning of the story. You are in a forest.
                \s
                Choice 1, 2
                Choice 2, 3
                Choice 3, 455\s\s
                """;
        getListFrom(input).forEach(levelParser::parse);
        Level level = levelParser.getLevel();
        Page page = level.getPage(1);
        assertEquals(1, page.pageNumber());
        assertEquals("Here is the beginning of the story. You are in a forest.", page.text());
        List<Choice> choices = page.choices();
        assertChoice(choices.get(0), "Choice 1", 2);
        assertChoice(choices.get(1), "Choice 2", 3);
        assertChoice(choices.get(2), "Choice 3", 455);

        String input2 = """
                \s
                5
                You are standing in a clearing. There is a satchel on the ground.
                Suddenly, a voice can be heard: "Run away now!". What do you do?
                \s
                Run away, 225
                Grab the satchel and run away, 35\s\s
                Stay where you are, 854
                \s
                9
                You are inside the Castle door. There is a noise from the cellar.
                \s
                Leave immediately, 289
                Explore the cellar, 192
                """;
        getListFrom(input2).forEach(levelParser::parse);
        level = levelParser.getLevel();

        page = level.getPage(5);
        assertEquals(5, page.pageNumber());
        String expectedText2 = "You are standing in a clearing. There is a satchel on the ground.\nSuddenly, a voice can be heard: \"Run away now!\". What do you do?";
        assertEquals(expectedText2, page.text());
        choices = page.choices();
        assertChoice(choices.get(0), "Run away", 225);
        assertChoice(choices.get(1), "Grab the satchel and run away", 35);
        assertChoice(choices.get(2), "Stay where you are", 854);

        page = level.getPage(9);
        assertEquals(9, page.pageNumber());
        String expectedText3 = "You are inside the Castle door. There is a noise from the cellar.";
        assertEquals(expectedText3, page.text());
        choices = page.choices();
        assertChoice(choices.get(0), "Leave immediately", 289);
        assertChoice(choices.get(1), "Explore the cellar", 192);

        assertEquals(3, level.getPages().size());
    }


    public List<String> getListFrom(String input){
       return Arrays.asList(input.split("\n"));
    }


    private void assertChoice(Choice choice, String expectedLabel, int expectedDestinationId){
        String actualLabel = choice.label();
        int actualDestinationId = choice.destinationPageNumber();

        assertEquals(expectedDestinationId, actualDestinationId);
        assertEquals(expectedLabel, actualLabel);
    }


    @Test
    public void canParseLine(){
        levelParser.initLevel();
        assertParserState( LevelParser.ParseState.PAGE_NUMBER);
        levelParser.parse("1");
        int pageNumber = 1;
        assertPageNumberParsed(pageNumber);
        assertParserState(LevelParser.ParseState.TEXT);

        String text = "This is a story all about how, my adventure got flip turned upside down";
        levelParser.parse(text);
        assertTextParsed(text);
        String text2 = "You are in a forest.";
        levelParser.parse(text2);
        String totalText = text + "\n" + text2;
        assertTextParsed(totalText);
        levelParser.parse(" ");
        assertParserState(LevelParser.ParseState.CHOICES);
        assertNumberOfChoices(0);
        levelParser.parse("Take the Left Path, 23");
        assertNumberOfChoices(1);
        levelParser.parse("Take the Right Path, 26");
        assertNumberOfChoices(2);
        levelParser.parse(" ");
        assertParserState(LevelParser.ParseState.PAGE_NUMBER);

        Level level = levelParser.getLevel();
        assertEquals(1, level.getPages().size());
        Page page = level.getPage(pageNumber);
        assertEquals(totalText, page.text());
        assertEquals(pageNumber, page.pageNumber());
        List<Choice> choices = page.choices();
        assertEquals(2, choices.size());
        assertChoice(choices.get(0), "Take the Left Path", 23);
        assertChoice(choices.get(1), "Take the Right Path", 26);
    }


    public void assertParserState(LevelParser.ParseState expectedState){
        try {
            Field field = LevelParser.class.getDeclaredField("currentParseState");
            field.setAccessible(true);
            //field.set(object, value);
            LevelParser.ParseState actualState = (LevelParser.ParseState)field.get(levelParser);
            assertEquals(expectedState, actualState);
        }catch (NoSuchFieldException | IllegalAccessException e){
            handleException(e);
        }
    }


    public void assertNumberOfChoices(int expected){
        try {
            Field field = LevelParser.class.getDeclaredField("currentChoices");
            field.setAccessible(true);
            List<?> actualChoices = (List<?>) field.get(levelParser);
            if(actualChoices == null){
                throw new RuntimeException("choices could not be extracted from parser for comparison");
            }
            assertEquals(expected, actualChoices.size());
        }catch (NoSuchFieldException | IllegalAccessException e){
            handleException(e);
        }
    }


    public void assertPageNumberParsed(int expectedPageNumber){
        try {
            Field field = LevelParser.class.getDeclaredField("currentPageNumber");
            field.setAccessible(true);
            Integer actualPageNumber = (Integer)field.get(levelParser);
            if(actualPageNumber == null){
                throw new RuntimeException("could not extract page number from parser to compare");
            }
            assertEquals(expectedPageNumber, (int)actualPageNumber);
        }catch (NoSuchFieldException | IllegalAccessException e){
           handleException(e);
        }
    }



    public void assertTextParsed(String expectedText){
        try {
            Field field = LevelParser.class.getDeclaredField("currentText");
            field.setAccessible(true);
            String actualText = (String)field.get(levelParser);
            assertEquals(expectedText, actualText);
        }catch (NoSuchFieldException | IllegalAccessException e){
            handleException(e);
        }
    }


    private void handleException(Exception e){
        System.out.println(e.getMessage());
    }


    @Test
    public void testLineParser(){
        invokeParseNumber("1");
        assertPageNumberParsed(1);

        invokeParseNumber(" 2");
        assertPageNumberParsed(2);

        invokeParseNumber(" 3   ");
        assertPageNumberParsed(3);
    }


    private void invokeParseNumber(String numberLine){
        try {
            Method method = LevelParser.class.getDeclaredMethod("parseNumber", String.class);
            method.setAccessible(true);
            method.invoke(levelParser, numberLine);

        }catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            handleException(e);
        }
    }

/*
    private void foo(Class targetClass,  ){

    }

 */
}
