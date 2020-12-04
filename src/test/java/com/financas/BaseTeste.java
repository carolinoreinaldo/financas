package com.financas;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

//@SpringBootTest -> Sobe todo o contexto do spring para fazer um único teste
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest // -> Cria uma transação separada para cada teste/delete os dados ao fim de cada teste
@AutoConfigureTestDatabase(replace = Replace.NONE) // -> O @DataJpaTest sobrescreve as configurações do application-test.properties,
//A anotação @AutoConfigureTestDatabase(NONE) não deixa sobrescrever as minhas configurações.
public class BaseTeste {

}
