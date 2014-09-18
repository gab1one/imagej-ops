package net.imagej.ops.condition;
 
import static org.junit.Assert.assertSame;
 
import org.junit.Test;
import org.scijava.Context;
 
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpService;
 
public class XorTest extends AbstractOpTest {
 
    @Test
    public void testXor()
    {
        Context ctx = new Context();
        OpService op = ctx.service(OpService.class);
         
         
        Condition<?> c1 =  (Condition<?>) op.op("and", 5.0, 4.0);
         
         
        Condition<?> c2 =  (Condition<?>) op.op("and", 5.0, 5.0);
         
         
        Object test = (op).run("xor", c2, c2);
        assertSame(test, false);
         
        Object test1 = (op).run("xor", c1, c2);
        assertSame(test1, true);
    }
}