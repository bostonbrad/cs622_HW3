public class SkipListNode {
    int value; // This will store the Document ID
    SkipListNode[] forward; // The pointers to next nodes

    public SkipListNode(int value, int level) {
        this.value = value;
        // level + 1 because arrays are 0-indexed (level 0 to level n)
        this.forward = new SkipListNode[level + 1];
    }
}