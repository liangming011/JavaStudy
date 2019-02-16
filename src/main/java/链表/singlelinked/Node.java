package 链表.singlelinked;


public class Node {

    //为了方便，这两个变量都使用public，而不用private就不需要编写get、set方法了。
    //存放数据的变量，简单点，直接为int型
    public int data;
    //存放结点的变量,默认为null
    public Node next;

    private Node head;

    //构造方法，在构造时就能够给data赋值
    public Node(int data) {
        this.data = data;
    }

    public Node() {
    }

    /**
     * 增加操作
     * 直接在链表的最后插入新增的结点即可
     * 将原本最后一个结点的next指向新结点
     */
    public Node addNode(Node node) {
        Node head = new Node(4);
        //System.out.println(head.data);
        //System.out.println(node.data);
        //链表中有结点，遍历到最后一个结点
        Node temp = head;    //一个移动的指针(把头结点看做一个指向结点的指针)
        while (temp.next != null) {    //遍历单链表，直到遍历到最后一个则跳出循环。
            temp = temp.next;        //往后移一个结点，指向下一个结点。
        }
        temp.next = node;    //temp为最后一个结点或者是头结点，将其next指向新结点
        //System.out.println(temp.next);
        //System.out.println(temp.next.data);
        return temp;
    }

    /**
     * insertNodeByIndex:在链表的指定位置插入结点。
     * 插入操作需要知道1个结点即可，当前位置的前一个结点
     * index:插入链表的位置，从1开始
     * node:插入的结点
     */
    public Node insertNodeByIndex(int index, Node node) {
        Node head = new Node(5);
        head = head.addNode(head);
        //首先需要判断指定位置是否合法，
        if (index < 1) {
            System.out.println("插入位置不合法。");
            return node;
        }
        int length = 1;            //记录我们遍历到第几个结点了，也就是记录位置。
        Node temp = head;        //可移动的指针
        while (node.next != null) {//遍历单链表
            if (index == length++) {        //判断是否到达指定位置。
                //注意，我们的temp代表的是当前位置的前一个结点。
                //前一个结点        当前位置        后一个结点
                //temp            temp.next     temp.next.next
                //插入操作。
                node.next = temp.next;
                temp.next = node;
                return temp;
            }
            temp = temp.next;
        }
        return temp;
    }

    /**
     * 通过index删除指定位置的结点,跟指定位置增加结点是一样的，先找到准确位置。然后进行删除操作。
     * 删除操作需要知道1个结点即可：和当前位置的前一个结点。
     *
     * @param index：链表中的位置，从1开始
     */
    public void delNodeByIndex(int index) {
        Node head = new Node(5);
        head = head.addNode(head);
        //判断index是否合理
        if (index < 1 || index > head.toString().length()) {
            System.out.println("给定的位置不合理");
            return;
        }

        //步骤跟insertNodeByIndex是一样的，只是操作不一样。
        int length = 1;
        Node temp = head;
        while (temp.next != null) {
            if (index == length++) {
                //删除操作。
                temp.next = temp.next.next;
                return;
            }
            temp = temp.next;
        }
    }

    /**
     * 对链表中的结点进行排序，按照从小到大的顺序，使用选择排序。
     * 使用双层遍历。第一层遍历，正常遍历链表，第二层遍历，遍历第一层遍历时所用的结点后面所有结点并与之比较
     * 选择排序比较简单，明白其原理，就能够写的出来。
     */
    public void selectSortNode() {
        Node head = new Node(5);
        head = head.addNode(head);
        //判断链表长度大于2，不然只有一个元素，就不用排序了。
        if (head.toString().length() < 2) {
            System.out.println("无需排序");
            return;
        }
        //选择排序
        Node temp = head;            //第一层遍历使用的移动指针，最处指向头结点，第一个结点用temp.next表示
        while (temp.next != null) {    //第一层遍历链表，从第一个结点开始遍历
            Node secondTemp = temp.next;        //第二层遍历使用的移动指针，secondTemp指向第一个结点，我们需要用到是第二个结点开始，所以用secondNode.next
            while (secondTemp.next != null) {//第二层遍历,从第二个结点开始遍历
                if (temp.next.data > secondTemp.next.data) {    //第二层中的所有结点依次与第一次遍历中选定的结点进行比较，
                    int t = secondTemp.next.data;
                    secondTemp.next.data = temp.next.data;
                    temp.next.data = t;
                }
                secondTemp = secondTemp.next;
            }
            temp = temp.next;
        }
    }

    /**
     * 对链表进行插入排序，按从大到小的顺序，只要这里会写，那么手写用数组插入排序
     * 也是一样的。先要明白原理。什么是插入排序，这样才好写代码。
     * 插入排序：分两组，一组当成有序序列，一组当成无序，将无序组中的元素与有序组中的元素进行比较(如何比较，那么就要知道插入排序的原理是什么这里不过多阐述)
     * 这里我想到的方法是，构建一个空的链表当成有序序列，而原先的旧链表为无序序列，按照原理，一步步进行编码即可。
     */
    public void insertSortNode() {
        Node head = new Node(5);
        head = head.addNode(head);
        //判断链表长度大于2，不然只有一个元素，就不用排序了。
        if (head.toString().length() < 2) {
            System.out.println("无需排序");
            return;
        }
        //创建新链表
        Node newHead = new Node(0);    //新链表的头结点
        Node newTemp = newHead;        //新链表的移动指针
        Node temp = head;        //旧链表的移动指针
        if (newTemp.next == null) {        //将第一个结点直接放入新链表中。
            Node node = new Node(temp.next.data);
            newTemp.next = node;
            temp = temp.next;    //旧链表中指针移到下一位(第二个结点处)。
        }
        while (temp.next != null) {     //    遍历现有链表
            while (newTemp.next != null) {
                //先跟新链表中的第一个结点进行比较,如果符合条件则添加到新链表，注意是在第一个位置上增加结点
                //如果不符合，则跟新链表中第二个结点进行比较，如果都不符合，跳出while，判断是否是到了新链表的最后一个结点，如果是则直接在新链表后面添加即可

                if (newTemp.next.data < temp.next.data) {
                    Node node = new Node(temp.next.data);
                    node.next = newTemp.next;
                    newTemp.next = node;
                    break;
                }
                newTemp = newTemp.next;
            }
            if (newTemp.next == null) {//到达最末尾还没符合，那么说明该值是新链表中最小的数，直接添加即可到链表中即可
                //直接在新链表后面添加
                Node node = new Node(temp.next.data);
                newTemp.next = node;
            }
            //旧链表指针指向下一位结点，继续重复和新链表中的结点进行比较。
            temp = temp.next;
            //新链表中的移动指针需要复位，指向头结点
            newTemp = newHead;
        }
        //开始使用新链表，旧链表等待垃圾回收机制将其收回。
        head = newHead;

    }

    /**
     * 计算单链表的长度，也就是有多少个结点
     *
     * @return 结点个数
     */
    public int length() {
        int length = 0;
        Node temp = head;
        while (temp.next != null) {
            length++;
            temp = temp.next;
        }
        return length;
    }

    /**
     * 遍历单链表，打印所有data
     */
    public void print() {
        Node temp = head.next;
        while (temp != null) {
            System.out.print(temp.data + ",");
            temp = temp.next;
        }
        System.out.println();
    }
}
