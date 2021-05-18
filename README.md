# syntax-analyzer
Este problema é um análisador sintático que faz uso deste [analisador léxico](https://github.com/UellingtonDamasceno/lexical-analyzer)
para identificar os padrão estabelecidos por essas [regex](https://github.com/UellingtonDamasceno/lexical-analyzer#lista-de-siglas)
e utiliza essa [grámatica](https://github.com/traozin/grammartica) para definir a ordem sintática dos tokens.

## Instalação
Para executar o projeto através do terminal, digite o seguinte comando no diretório
raiz do projeto: 

    mvn clean install
  
Em seguida, acesse a pasta `target` e lá deverá conter um arquivo 
`syntax-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar` que corresponde ao 
executável do projeto.

Para iniciar o jar basta executar o seguinte comando:
  
    java -jar syntax-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar
  
 <details>
 <summary>Deseja executar o projeto pelo netbeans?</summary>
 <br>
 <p>
 Para executar esse <i>software</i> é necessário clonar esse repositório. Após isso, já no netbeans acesse aos menus: 
 `files` > `open project` > selecione o projeto(no diretório onde foi salvo) > Abra o projeto > "Construa" o mesmo.
 </p>
 </details>

## Parâmetros de inicalização
 Por padrão o este _software_ oculta no arquivo de saída os tokens obtidos pelos
 [analisador léxico](https://github.com/UellingtonDamasceno/lexical-analyzer). 
 Porém é possível inserir a flag `-l` como no comando abaixo para indicar que 
 deseja visualizar os tokens. 
 
    java -jar syntax-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar -l
    
  
  > :warning: Vale resaltar que o software só aceita a flag `-l` e ela DEVE, OBRIGATORIAMENTE
 ser a PRIMEIRA flag. Caso contário os tokens não serão exibidos.
 

 [Veja mais detalhes sobre o arquivo de saída](#saída)
 
 ## Entrada
 
 Este analisador é capaz de ler multiplos arquivos de texto, contanto que respeite
 as seguintes regras: 
 - Está em uma pasta chamada `input` que deve está contida no mesmo diretório do `jar`;
 - Todos os arquivos devem ser nomeados com o préfixo `entrada` seguido de um número;
 - Todos os arquivos serem `.txt`;
 
 
 ### Exemplo de entrada
 
 ```java
 var{
  int i = 10, j = 10;
 }
procedure start {
  print("HELLO WORLD!");
} 
 ```
 ## Saída
 
 Será gerado um conjunto de arquivos de saída, denomidado saidaX.txt, onde X é um
 valor numérico, referente ao arquivo de entrada que ele representa.
 
 ### Exemplo de saída
 
 Utilizando o exemplo de entrada utilizado na sessão anterior obteremos a seguite
 saida caso não seja utilizado a flag `-l`:
 
    `NÃO FORAM ENCONTRADOS ERROS SINTÁTICOS!`.
 
 Ao remover último `}` do exemplo apresentado anteriormente obteremos o seguinte 
 resultado caso não seja utilizado a flag `-l`:
 
    `O arquivo terminou inesperadamente! O analisador esperava um: "}"` 
 
 ## Tratamento de erros
 A abordagem de tratamento de erros utilzadas por esse problema foi o modo pânico 
 com tokens de sincronização para definir os momentos em que o analisador 
 deve parar de descartar os tokens inválidos e voltar ao fluxo normal de consumo de 
 produções.
 
 ### Tokens de sincronização
 O conjunto de tokens de sincronização varia de acordo com a produção que está
 sendo consumida no momento. 
 
 Mas podemos observar que os token sincronizadores da produção `global statments` são
 os `{`, `procedure`, `function`, `var`, `const` e `typedef` esses tokens foram selecionados
 porque tinham um a taxa de descarte de tokens baixa e são os iniciais das produções 
 que compõe a produção `global statments`.
