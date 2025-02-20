package io.project.Bot.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;


public class SpecialList<K, V> extends ArrayList<Map.Entry<K, V>>{
    ArrayList<Map.Entry<K, V>> list = new ArrayList<>();
    private int capacity;

    private SpecialList(int capacity) {
        this.capacity = capacity;
    }

    public static <K, V> SpecialList<K, V> createSpecialList(int capacity, Class<K> keyType, Class<V> valueType) {
        return new SpecialList<>(capacity);
    }

    @Override
    public boolean add(Map.Entry<K, V> entry) {
        if (size()==capacity) remove(0);
        super.add(entry);

        return true;
    }

    public static void main(String[] args) {
        SpecialList specialList =
                SpecialList.createSpecialList(2, String.class, String.class);
        for(int i = 0; i<30; i++)
        specialList.getRandom();
    }

    public Map.Entry<K, V> getRandom(){
        Random rn = new Random();
        int randomNum = rn.nextInt(capacity);
        System.out.println(randomNum);
        return null;// get(randomNum);
    }
}
