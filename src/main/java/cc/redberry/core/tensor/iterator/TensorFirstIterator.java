package cc.redberry.core.tensor.iterator;

import cc.redberry.core.tensor.Tensor;

/*
* Wrapper for TreeTraverseIterator. Return only <blockquote>
*/
public class TensorFirstIterator extends TensorIterator {
    public TensorFirstIterator(Tensor tensor) {
        super(tensor);
    }

    public TensorFirstIterator(Tensor tensor, TraverseGuide guide) {
        super(tensor, guide);
    }

    public Tensor next(){
        return super.next(TraverseState.Leaving);
    }
}
