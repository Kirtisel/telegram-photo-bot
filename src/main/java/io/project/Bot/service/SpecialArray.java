package io.project.Bot.service;

import java.util.Random;

public class SpecialArray {
    private final int length;
    private final int height;
    private final Object[][] objArray;
    private final Class<?>[] types;
    private int currentIndex;
    private final Random random = new Random();

    private SpecialArray(int height, Class<?>[] types) {
        this.length = types.length;
        this.height = height;
        this.objArray = new Object[height][types.length];
        this.types = types;
        this.currentIndex = 0;
    }

    public static SpecialArray
    createSpecialArray(int height, Class<?> ... types){//Class<A>[] aType, Class<B> bType, Class<C> cType) {
        return new SpecialArray(height, types);
    }

    public static void main(String[] args) {
        SpecialArray array = SpecialArray.createSpecialArray(50, String.class, Integer.class, String.class);

        for (int i = 0; i < 50; i++)
        array.add("a", i, "");

        //System.out.println(array.types[0]);
        //System.out.println(array.types[0].getClass());
        //System.out.println("a".getClass());
        for (int i = 0; i < 50; i++)
        System.out.println(array.get(i)[1]);

        System.out.println();
        array.add("b",1, "");
        System.out.println(array.get(49)[0]);

        System.out.println();
        for (int i = 0; i < 50; i++)
            System.out.println(array.get(i)[1]);

        System.out.println();
        System.out.println("random = ");
            System.out.println(array.getRandom()[1]);

    }

    private void moveDown(){
//        for(int i = 1; i < height; i++){
//            for (int k = 0; k < length; k++){
//                objArray[i-1][k] = objArray[i][k];
        if (height > 1) {
            System.arraycopy(objArray, 1, objArray, 0, height - 1);
        }


    }


    public boolean add (Object ... objects){
        if (objects.length != length) throw new IllegalArgumentException("Too much or not enough arguments");

        if (currentIndex == height) {
            if (height == 1) {
                currentIndex = 0; // Сбрасываем индекс, если высота равна 1
            } else {
                currentIndex--;
                moveDown();
            }
        }

        for(int i = 0; i < length; i++){
            if (types[i].isInstance(objects[i])) {
                throw new IllegalArgumentException("Class to add and class declared in createSpecialArray() are not same");
            }
            objArray[currentIndex][i] = objects[i];
        }

        currentIndex++;
        return true;
    }

    public Object[] get (int index){
        if (index < 0 || index >= height) throw new IndexOutOfBoundsException();
     return objArray[index];
    }

    public Object[] getRandom(){
        int randomNum = random.nextInt(height);
        return get(randomNum);// get(randomNum);
    }

}
