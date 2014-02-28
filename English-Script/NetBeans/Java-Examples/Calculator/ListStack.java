public class ListStack<Element> extends Stack<Element>
{
    class DataPointerPair
    {
        Element         mData;
        DataPointerPair mNextPointer;
        
        DataPointerPair(Element data, DataPointerPair nextPointer) {
            mData        = data;
            mNextPointer = nextPointer;
        }
        
        DataPointerPair recursiveCopy(DataPointerPair pointer) {
            if (pointer == null) {
                return pointer;
            } else {
                Element data = pointer.mData;
                DataPointerPair nextPointer = recursiveCopy(pointer.mNextPointer);
                return new DataPointerPair(data, nextPointer);
            }
        }
    }

    DataPointerPair mPointer;
    
    public ListStack() {
        mPointer = null;
    }
    
    public void push(Element e) {
        mPointer = new DataPointerPair(e, mPointer);
    }
    
    public void pop() {
        assert mPointer != null : "Stack underflow!";
        mPointer = mPointer.mNextPointer;
    }

    public Element top() {
        assert mPointer != null : "Stack is empty!";
        return mPointer.mData;
    }
    
    public boolean isEmpty() {
        return mPointer == null;
    }   

    public ListStack<Element> clone() throws CloneNotSupportedException {
        ListStack<Element> result = new ListStack<Element>();
        if (mPointer != null) {
            result.mPointer = mPointer.recursiveCopy(mPointer);
        }
        return result;
    }
}


    