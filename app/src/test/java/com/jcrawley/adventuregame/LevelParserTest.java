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
                [1. Here is the beginning of the story. You are in a forest.
                - Choice 1, 2
                - Choice 2, 3
                - Choice 3, 455\s\s
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
              
                [5. You are standing in a clearing. There is a satchel on the ground.
                Suddenly, a voice can be heard: "Run away now!". What do you do?
                -Run away, 225
                -Grab the satchel and run away, 35
                -Stay where you are, 854
              
                [9. You are inside the Castle door. There is a noise from the cellar.
                -Leave immediately, 289
                -Explore the cellar, 192
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

}
