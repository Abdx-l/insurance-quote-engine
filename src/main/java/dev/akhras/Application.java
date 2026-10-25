package dev.akhras;
import org.springframework.boot.*;import org.springframework.boot.autoconfigure.*;import org.springframework.context.annotation.*;import org.springframework.web.bind.annotation.*;import org.springframework.data.jpa.repository.JpaRepository;import jakarta.persistence.*;import java.math.*;import java.util.*;
@SpringBootApplication @RestController public class Application{
 public static void main(String[]a){SpringApplication.run(Application.class,a);}
 private final Factors factors;Application(Factors f){factors=f;}
 @Entity public static class Factor{@Id public String name;public int minimumAge,maximumAge;public BigDecimal multiplier;public Factor(){}Factor(String n,int lo,int hi,String m){name=n;minimumAge=lo;maximumAge=hi;multiplier=new BigDecimal(m);}}
 @Bean CommandLineRunner seed(Factors f){return a->{if(f.count()==0)f.saveAll(List.of(new Factor("age 18-34",18,34,"1.00"),new Factor("age 35-44",35,44,"1.20"),new Factor("age 45-65",45,65,"1.60")));};}
 public record Request(int age,boolean smoker,BigDecimal coverage,int term){}
 public record Quote(BigDecimal premium,List<String> applied){}
 public static Quote calculate(Request r,List<Factor>factors){
  if(r.age<18||r.age>65||r.coverage==null||r.coverage.compareTo(new BigDecimal("10000"))<0||r.coverage.compareTo(new BigDecimal("1000000"))>0||r.term<5||r.term>30)throw new IllegalArgumentException("invalid quote request");
  var factor=factors.stream().filter(f->r.age>=f.minimumAge&&r.age<=f.maximumAge).findFirst().orElseThrow(()->new IllegalArgumentException("no age factor"));
  var smoking=r.smoker?new BigDecimal("1.80"):BigDecimal.ONE;var term=BigDecimal.ONE.add(BigDecimal.valueOf(r.term-5).multiply(new BigDecimal(".01")));
  var premium=r.coverage.divide(new BigDecimal("1000")).multiply(new BigDecimal("2.50")).multiply(factor.multiplier).multiply(smoking).multiply(term).setScale(2,RoundingMode.HALF_UP);
  return new Quote(premium,List.of("base: 2.50 per 1000 coverage",factor.name+": x"+factor.multiplier,"smoker: x"+smoking,"term: x"+term));
 }
 @PostMapping("/quote")public Quote quote(@RequestBody Request r){return calculate(r,factors.findAll());}
 @ExceptionHandler(IllegalArgumentException.class) @ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)public Map<String,String> error(IllegalArgumentException e){return Map.of("error",e.getMessage());}
}
interface Factors extends JpaRepository<Application.Factor,String>{}
