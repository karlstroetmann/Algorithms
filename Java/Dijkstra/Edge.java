class Edge 
{
    private Node    mSource;
    private Node    mTarget;
    private Integer mLength;
    
    public Edge(Node source, Node target, Integer length) {
        mSource = source;
        mTarget = target;
        mLength = length;
    }    
    public Node    getSource() { return mSource; }
    public Node    getTarget() { return mTarget; }
    public Integer getLength() { return mLength; }
    public String  toString () {
        return "<" + mSource + ", " + mTarget + ">: " + mLength;
    }
}
