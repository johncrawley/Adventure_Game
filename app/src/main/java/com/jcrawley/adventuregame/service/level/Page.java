package com.jcrawley.adventuregame.service.level;

import java.util.List;

public record Page(int pageNumber, int parentPageNumber, String text, List<Choice> choices){}
