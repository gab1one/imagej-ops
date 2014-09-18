package net.imagej.ops.condition;
 
import static org.junit.Assert.assertSame;
 
import java.util.ArrayList;
 
import org.junit.Test;
import org.scijava.Context;
 
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpService;
 
public class UnionTest<T> extends AbstractOpTest {
 
    @Test
    public void testUnion()
    {
        ArrayList<Condition<T>> condition = new ArrayList<Condition<T>>();
         
        Context ctx = new Context();
        OpService op = ctx.service(OpService.class);
         
         
        Condition<?> c1 =  (Condition<?>) op.op("and", 5.0, 4.0);
         
         
        Condition<?> c2 =  (Condition<?>) op.op("and", 5.0, 5.0);
         
        condition.add((Condition<T>) c1);
        condition.add((Condition<T>) c2);
         
        Boolean result = (Boolean) (op).run("union", condition);
        assertSame(result, true);
         
        condition.add(1, (Condition<T>) c1);
        Boolean result1 = (Boolean) (op).run("union", condition);
        assertSame(result1, false);
 
    }
}