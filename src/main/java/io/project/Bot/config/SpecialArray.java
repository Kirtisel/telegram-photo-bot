package io.project.Bot.config;


public class SpecialArray {
    private int length;
    private int height;
    private Object[][] objArray;
    public Object[] types;
    private int iter;
//    A[] A;
//    B[] B;
//    C[] C;




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
        SpecialArray array = SpecialArray.createSpecialArray(3, String.class, Integer.class, String.class);
        array.add("a", 1, "");
        array.add("b", 2, "");
        array.add("c", 3, "");
        //System.out.println(array.types[0]);
        //System.out.println(array.types[0].getClass());
        //System.out.println("a".getClass());
        System.out.println(array.get(0)[0]);
        System.out.println(array.get(1)[0]);
        System.out.println(array.get(2)[0]);
    }

//    private void deleteFirstElement(){
//        for(int i = 0; i < length; i++){
//            objArray[0][i] = null;
//        }
//    }

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

}
