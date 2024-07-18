package com.jcrawley.adventuregame.service.level;

import java.util.List;

public record Page(int pageNumber, String text, List<Choice> choices){}
