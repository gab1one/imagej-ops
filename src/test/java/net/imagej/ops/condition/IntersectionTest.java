package net.imagej.ops.condition;
 
import static org.junit.Assert.assertSame;

import java.util.ArrayList;

import org.junit.Test;
import org.scijava.Context;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpService;
 
public class IntersectionTest<T> extends AbstractOpTest {
 
    @Test
    public void testIntersection()
    {
         
    	ArrayList<Condition<T>> condition = new ArrayList<Condition<T>>();
         
    	Condition<?> c1 =  (Condition<?>) ops.op(FunctionGreaterCondition.class, 5.0, 3.0);
    	Condition<?> c2 =  (Condition<?>) ops.op(FunctionLessCondition.class, 5.0, 6.0);

        condition.add((Condition<T>) c1);
        condition.add((Condition<T>) c2);
         
        Boolean result = (Boolean) ops.run(IntersectionCondition.class, condition);
        assertSame(result, false);
         
        condition.add(0, (Condition<T>) c2);
        Boolean result1 = (Boolean) ops.run(IntersectionCondition.class, condition);
        assertSame(result1, true);
         
         
    }
}