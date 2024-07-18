package com.jcrawley.adventuregame.service.level;

import java.util.HashMap;
import java.util.Map;

public class Level {


    /*
        You are in a room with three doors, what door should you take
            Door 1 = page 17
            Door 2 = page 25
            Door 3 = item: key page 26


     */
    private Map<Integer, Page> pageMap;

    public Level(){
        pageMap = new HashMap<>();
    }


    public void addPage(Page page){
        pageMap.put(page.pageNumber(), page);
    }


    public Page getPage(int pageNumber){
        return pageMap.get(pageNumber);
    }

    public Map<Integer, Page> getPages(){
        return pageMap;
    }
}
