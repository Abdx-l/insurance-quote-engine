package dev.akhras;
import org.junit.jupiter.params.ParameterizedTest;import org.junit.jupiter.params.provider.ValueSource;import org.junit.jupiter.api.Test;import static org.junit.jupiter.api.Assertions.*;import java.math.*;import java.util.*;
class QuoteTest{
 List<Application.Factor>f=List.of(new Application.Factor("young",18,34,"1"),new Application.Factor("middle",35,44,"1.2"),new Application.Factor("older",45,65,"1.6"));
 @ParameterizedTest @ValueSource(ints={18,34,35,44,45,65})void edges(int age){assertTrue(Application.calculate(new Application.Request(age,false,new BigDecimal("10000"),10),f).premium().signum()>0);}
 @Test void invalid(){assertThrows(IllegalArgumentException.class,()->Application.calculate(new Application.Request(17,false,new BigDecimal("10000"),10),f));}
}
