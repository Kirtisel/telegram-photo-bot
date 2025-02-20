package io.project.Bot.config;


import java.util.Map;
import java.util.Random;

public class SpecialArray {
    private int length;
    private int height;
    private Object[][] objArray;
    private Object[] types;
    private int iter;

    private SpecialArray(int height, Object[] types) {
        this.length = types.length;
        this.height = height;
        this.objArray = new Object[height][types.length];
        this.types = types;
        this.iter = 0;
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
        for(int i = 1; i < height; i++){
            for (int k = 0; k < length; k++){
                objArray[i-1][k] = objArray[i][k];
            }
        }
    }


    public boolean add (Object ... objects){
        if (objects.length != length) throw new IllegalArgumentException("Too much or not enough arguments");

        if(iter==height){
            // написать удаление элемента
            iter--;
            moveDown();
        }

        for(int i = 0; i < length; i++){
            if (types[i] != objects[i].getClass()) {
                throw new IllegalArgumentException("Class to add and class declared in createSpecialArray() are not same");
            }
            objArray[iter][i] = objects[i];
        }

        iter++;
        return true;
    }

    public Object[] get (int index){
        if (index < 0 || index > height-1) throw new IndexOutOfBoundsException();

        Object[] arr = new Object[length];
     for (int i = 0; i < length; i++) {
         arr[i] = objArray[index][i];
     }

     return arr;
    }

    public Object[] getRandom(){
        Random rn = new Random();
        int randomNum = rn.nextInt(height);
        return get(randomNum);// get(randomNum);
    }

}
