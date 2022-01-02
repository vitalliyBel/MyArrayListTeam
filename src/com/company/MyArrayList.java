package com.company;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class MyArrayList <T> implements Comparable<MyArrayList> {
    //Инициализированная емкость по умолчанию
    private static final int  DEFAULT_CAPACITY = 10;
    //Максимальный размер MyArrayList
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    //Массив для инициализации elementData до первого добавленного элемента
    private static final Object[] EMPTY_ELEMENT_DATA = {};
    //Массив элементов MyArrayList
    private Object[] elementData;
    //Количество элементов MyArrayList
    private int size;

    //Конструктор для инициализации массива/списка необходимой емкости
    public MyArrayList(int initialCapacity){
        if(initialCapacity > 0)
            this.elementData = new Object[initialCapacity];
        else
            throw new IllegalArgumentException(String.valueOf(initialCapacity));
    }
    //Конструктор без параметров
    public MyArrayList(){
        this.elementData = EMPTY_ELEMENT_DATA;
    }


    //Добавляет новый элемент в конец списка. Возвращает boolean-значение
    public boolean add(T value){
        /*
         *Перед тем, как вставить добавляемый элемент в список делается проверка: достаточно ли места для вставки.
         *Если массив, хранящий элементы, переполнен и для добавления элемента в список нет места, то
         *список увеличивается в размере.
         */
        capacityInternal(size+1);
        elementData[size++] = value;
        // Если хватает места для вставки, то происходит добавление элемента в конец, при этом возвращается true
        return true;
    }
    private void capacityInternal(int minCapacity){
        /*
         *Если текущий элемент данных является пустым массивом,
         *то требуемая емкость и максимальное значение емкости
         *по умолчанию используются в качестве требуемой емкости
         */
        if(elementData == EMPTY_ELEMENT_DATA)
            minCapacity = Math.max(minCapacity,DEFAULT_CAPACITY);
        //Проверка необходимости расширения
        checkForExtension(minCapacity);
    }
    private void checkForExtension(int minCapacity){
        //Если требуемая емкость больше длины текущего массива, выполните расширение
        if(minCapacity - elementData.length > 0)
            grow(minCapacity);
    }
    //Расширяем массив
    private void grow(int minCapacity){
        int oldCapacity = elementData.length;
        //Размер будет увеличен в полтора раза
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        /*
         *Если новый размер меньше минимально возможного размера,
         *то новый размер делается равным минимальному необходимому размеру.
         */
        if(newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        //Если новый размер больше максимально возможного размера, то бросается ошибка
        if (newCapacity - MAX_ARRAY_SIZE > 0 )
            throw new OutOfMemoryError();
        //Помещаем элементы массива в новый массив
        elementData = Arrays.copyOf(elementData,newCapacity);
    }
    //Проверка на то, что индекс, по которому происходит вставка, не выходит за границы списка
    private void checkIndex ( int index, int size){
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException(String.valueOf(index));
    }
    /*
     *Метод добавляет элемент element в позицию index.
     *При добавлении происходит сдвиг всех элементов справа от указанного индекса на 1 позицию вправо
     */
    public void add(int index,T value){
        //Проверка на то, что индекс, по которому происходит вставка, не выходит за границы списка
        checkIndex(index,size);
        //Проверка необходимости расширения
        capacityInternal(size + 1);
        /*
         *Массив подготавливается для вставки.
         *Все элементы, находящиеся справа от указанного индекса, будут сдвигаться на одну позицию вправо (index+1).
         *И только после этого во внутренний массив под указанным индексом добавляется новый элемент.
         */
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        //Вставка значения по указанному индексу
        elementData[index] = value;
        size++;
    }
    /*
     *Удаление элемента в указанной позиции индекса.
     *После удаления сдвигает все элементы влево для заполнения освободившегося пространства.
     */
    public Object remove(int index){
        //Проверка на то, что индекс, по которому происходит удаление, не выходит за границы списка
        checkIndex(index,size);
        Object oldValue = elementData[index];
        //Количество элементов, которые необходимо скопировать
        int numMoved = size - index - 1;
        System.out.println(size);
        System.out.println(numMoved);
        //Происходит смещение всех элементов влево
        if(numMoved > 0)
            System.arraycopy(elementData,index+1,elementData,index, numMoved);
        //Получается 'сдвиг' влево. Последний элемент уже не нужен, поэтому его отдаем на 'съедение' GarbageCollectror-у.
        elementData[--size] = null;
        return oldValue;
    }
    /*
     *Метод удаляет из списка переданный элемент
     *При удалении по значению происходит поиск удаляемого элемента.
     *Идет перебор массива поэлементно, пока не будет найден необходимый.
     *Если элемент присутствует в списке, он удаляется, а все элементы смещаются влево.
     *Если элемент существует в списке и успешно удален, метод возвращает true, в обратном случае — false.
     */
    public boolean removeByValue(Object value){
        for (int i = 0;i < elementData.length;i++){
            if (value.equals(elementData[i])) {
                fastRemove(i);
                return true;
            }
        }
        return false;
    }
    private void fastRemove(int index){
        //Количество элементов, которые необходимо скопировать
        int numMoved = size - index - 1;
        //Происходит смещение всех элементов влево
        if (numMoved > 0)
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        //После сдвига влево, последний элемент уже не нужен.
        elementData[--size] = null;
    }
    // Возвращает элемент, который расположен в указанной позиции списка.
    public Object get(int index) {
        //проверка на то, что индекс, по которому происходит возврат элемента, не выходит за границы списка
        checkIndex(index, size);
        return elementData[index];
    }
    /*Метод получает из списка переданный элемент
     *При получении по значению происходит поиск возвращаемого элемента.
     *Идет перебор массива поэлементно, пока не будет найден необходимый.
     *Если элемент существует в списке и успешно получен, метод возвращает true, в обратном случае — false.
     */
    public Boolean get(Object value) {
        for (int i = 0; i < elementData.length; i++) {
            if (value.equals(elementData[i]))
                return true;
        }
        return false;
    }
    /*
     *Данный метод урезает размер массива до количества элементов.
     *Позволяет избежать утечек памяти т.к. несмотря на то, что элементы из списка мы удалили
     *размер списка/количество доступных ячеек в массиве не изменится.
     */
    public void trimToSize(){
        if (size < elementData.length){
            elementData = Arrays.copyOf(elementData,size);
        }
    }
    //Возвращает количество элементов
    public int getSize() {
        return size;
    }
    //Возвращает длину массива/списка
    public int getLength () {
        return this.elementData.length;
    }

    /*
     *Метод Swap, будет менять местами значения в массиве по индексу.
     */
    private  void swap(int first, int second){
        Object dummy = elementData[first];
        elementData[first] = elementData[second];
        elementData[second] = dummy;
    }
    // алгоритм сортировки пузырьком
    public <T extends Comparable<T>> void sort() {
        //урезаем размер массива до количества элементов
        trimToSize();
        // сортируется массив элементов в порядке возрастания(по умолчанию)
        Arrays.sort(elementData);
        for (int i = elementData.length - 1; i >= 1; i--) {
            for (int j = 0; j < i; j++) {
                int result =((T) elementData[j]).compareTo((T) elementData[j+1]);
                if (result > 0)
                    swap(j, j + 1);
            }
        }
    }
    /*
    *Сравнивает вызывающий объект с объектом, переданным в качестве параметра,
    */
    @Override
    public int compareTo(MyArrayList o) {
        return this.size - o.size;
    }
    //Comparator для сортировки списка или массива объектов по количеству элементов
    public static Comparator<MyArrayList> ComparatorBySize = new Comparator<MyArrayList>() {
        @Override
        public int compare(MyArrayList a, MyArrayList b) {
            return a.getSize()-b.getSize();
        }
    };
    //Comparator для сортировки списка или массива объектов по длине списка/массива
    public static Comparator<MyArrayList> ComparatorByLength = new Comparator<MyArrayList>() {
        @Override
        public int compare(MyArrayList a, MyArrayList b) {
            return a.getLength()-b.getLength();
        }
    };
}


