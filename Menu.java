import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Scanner;


public class Menu {
    static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    public static void main(String[] args) throws Exception{
        menu();
    }

    public static void menu() throws Exception{
        Scanner sc = new Scanner(System.in);
        int op;
        //Interação com o usuário fornecendo as opções do CRUD
        System.out.println("\nEscolha uma das opções abaixo:");
        System.out.println("0 - Encerrar");
        System.out.println("1 - Carregar a base de dados");
        System.out.println("2 - Ler o arquivo");
        System.out.println("3 - Cadastrar novo registro");
        System.out.println("4 - Ler registro");
        System.out.println("5 - Atualizar registro");
        System.out.println("6 - Deletar registro");
        System.out.print("Opção: ");
        op = Integer.parseInt(sc.nextLine());

        if(op == 1){
            read();
        }else if(op == 2){
            readArq("arquivo.txt"); //percorre o arquivo
        }else if(op == 3){
            Movies novo = new Movies(); //novo objeto é criado
            
            System.out.println("Campos a serem preenchidos: ");
            //validaçao do id escolhido pelo usuario
            boolean existe = true;
            do{
                System.out.println("Id: ");
                int id = Integer.parseInt(sc.nextLine());
                existe = readId(id);
                if(existe){
                    System.out.println("Id já utilizado! Tente novamente.");
                }else{
                    novo.setId(id);
                }
            }while(existe);
            System.out.println("Data: (yyyy-MM-dd)");
            String data1 = sc.nextLine();
            novo.setDate(data1); 
            System.out.println("Language: ");
            String lg = sc.nextLine();
            novo.setLanguage(lg);
            System.out.println("Title: ");
            String tt = sc.nextLine();
            novo.setTitle(tt);
            System.out.println("Average: ");
            double av = Double.parseDouble(sc.nextLine());
            novo.setAverage(av);
            System.out.println("Gender: ");
            String gd = sc.nextLine();
            novo.setGender(gd);
            writeArq(novo, "arquivo.txt"); //metodo que passa o objeto para o arquivo 
        }else if(op == 4){
            System.out.println("Digite o id: ");
            int id = sc.nextInt();
            readId(id);
        }else if(op == 5){
            Movies novo = new Movies(); //cria-se o novo objeto
            System.out.println("Digite o id do registro que você deseja alterar: ");
            int id = Integer.parseInt(sc.nextLine());
            novo.setId(id);
            System.out.println("Campos a serem alterados: ");
            System.out.println("Data: (yyyy-MM-dd)");
            String data1 = sc.nextLine();
            novo.setDate(data1); 
            System.out.println("Language: ");
            String lg = sc.nextLine();
            novo.setLanguage(lg);
            System.out.println("Title: ");
            String tt = sc.nextLine();
            novo.setTitle(tt);
            System.out.println("Average: ");
            double av = Double.parseDouble(sc.nextLine());
            novo.setAverage(av);
            System.out.println("Gender: ");
            String gd = sc.nextLine();
            novo.setGender(gd);
            update(novo);
        }else if(op == 6){
            System.out.println("Digite o id: ");
            int id = sc.nextInt();
            delete(id);
        }else if(op==7){
            intercalacaoBalanceadaComum();
        }
        if(op>0){
            menu();
        }
        sc.close();
    }
    
    public static void read() throws Exception{ //leitura da base de dados
        try {
            // Leitura do arquivo CSV
            String basefile = "./Top_10000_Movies.csv";

            FileInputStream fstream = new FileInputStream(basefile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            // Leitura das linhas do arquivo CSV de filmes
            String line;
  
            while((line = br.readLine()) != null) {
                Movies movie = new Movies();
                movie.assignment(line);
                writeArq(movie, "arquivo.txt"); //escreve um objeto de cada vez no arquvio de bytes
            }

            System.out.println("Base carregada com sucesso!");

            // Fechar a leitura CSV
            fstream.close();
        }
        catch(IOException e) { e.printStackTrace(); }
    }

    public static void writeArq(Movies movie, String arquivo){ //escreve no fim do arquvio 
        try {
            RandomAccessFile arq = new RandomAccessFile(arquivo, "rw");
            //antes de escrever o novo registro no arquvio, tem que alterar o cabecalho
            arq.seek(0);
            arq.writeInt(movie.getId());
            
            arq.seek(arq.length());
        
            byte[] arrayByte = movie.toByteArray();
            boolean lapide=true;

            arq.writeBoolean(lapide);
            arq.writeInt(arrayByte.length); //Tamanho do registro em bytes
            arq.write(arrayByte);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //return movie.getId();
    }

    public static void writeArqPos(Movies movie, int pos){ //escreve um registro no arquivo na posicao desejada, esse metodo
        //é utilizado apenas quando o usuario deseja alterar algum registro e o tamanho continua o mesmo
        try {
            RandomAccessFile arq = new RandomAccessFile("arquivo.txt", "rw");
            arq.seek(pos);

            byte[] arrayByte = movie.toByteArray();
            boolean lapide=true;

            arq.writeBoolean(lapide);
            arq.writeInt(arrayByte.length); //Tamanho do registro em bytes
            arq.write(arrayByte);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void readArq(String arquivo) throws IOException{ //leitura do arquivo inteiro - todos os registros validos - os que não foram apagados
        RandomAccessFile arq = new RandomAccessFile(arquivo, "rw");
        
        arq.seek(0); //posiciona o ponteiro no inicio do arquivo = cabecalho
        int cabecalho = arq.readInt(); //descobre qual e o ultimo registro do arquivo 
        int pos = 4; //posicao do primeiro registro
        int tam = 0;
        Movies temp = new Movies();
        boolean acabou = false;

        do{
            arq.seek(pos); //posiciona o ponteiro no inicio do proximo registro
            boolean lapide = arq.readBoolean(); //leitura da lapide
            tam = arq.readInt(); //leitura do tamanho do registro
            if(lapide==true){ //verifica se o registro e valido
                byte[] arrayByte = new byte[tam]; 
                arq.read(arrayByte); //leitura do array de bytes
                temp.fromByteArray(arrayByte); //transforma o array de bytes em um objeto Movite
                String dataFormatada = df.format(temp.getDate());
                System.out.println(lapide + " " + temp.getId() + " " + dataFormatada + " " + temp.getLanguage()+ " " + temp.getTitle() + " " + temp.getAverage() + " " + temp.getGender()); 
                if(temp.getId()==cabecalho){
                    acabou = true;
                }
            } 
            pos += tam + 4 + 1; //adiciona a quantidade de bytes para chegar no inicio do proximo registro
        }while(!acabou);    
    }

    public static boolean readId(int id) throws IOException{ //leitura de um unico registro de acordo com o id informado
        RandomAccessFile arq = new RandomAccessFile("arquivo.txt", "rw");
        
        boolean achou = false;
        arq.seek(0); //posiciona o ponteiro no inicio do arquivo = cabecalho
        int cabecalho = arq.readInt(); //descobre qual e o ultimo registro do arquivo 
        int pos = 4; //posicao do primeiro registro
        int tam = 0;
        Movies temp = new Movies();

        do{
            arq.seek(pos); //posiciona o ponteiro no inicio do proximo registro
            boolean lapide = arq.readBoolean(); //leitura da lapide
            tam = arq.readInt(); //leitura do tamanho do registro
            if(lapide==true){ //verifica se o registro e valido
                byte[] arrayByte = new byte[tam]; 
                arq.read(arrayByte); //leitura do array de bytes
                temp.fromByteArray(arrayByte); //transforma o array de bytes em um objeto Movite
                if(temp.getId()==id){ //testa se e o registro procurado
                    achou = true;
                    String dataFormatada = df.format(temp.getDate());
                    System.out.println("Registro encontrado! Dados do registro: ");
                    System.out.println(temp.getId() +" "+ dataFormatada + " " + temp.getLanguage()+ " " + temp.getTitle() + " " + temp.getAverage() + " " + temp.getGender()); 
                }
            }
            pos += tam + 4 + 1; //adiciona a quantidade de bytes para chegar no inicio do proximo registro
        }while(temp.getId()!=cabecalho && !achou);  
        if(!achou){
            System.out.println("Registro não encontrado!");
        }
        return achou;
    }

    public static Movies readPos(int pos, String arquivo) throws IOException{ //leitura de um unico registro de acordo com o id informado
        RandomAccessFile arq = new RandomAccessFile(arquivo, "rw");

        Movies temp = new Movies();
        arq.seek(pos); //posiciona o ponteiro no inicio do proximo registro
        boolean lapide = arq.readBoolean(); //leitura da lapide
        int tam = arq.readInt(); //leitura do tamanho do registro
        if(lapide==true){ //verifica se o registro e valido
            byte[] arrayByte = new byte[tam]; 
            arq.read(arrayByte); //leitura do array de bytes
            temp.fromByteArray(arrayByte); //transforma o array de bytes em um objeto Movite
        }
        return temp;
    }

    public static boolean delete(int id) throws IOException{
        RandomAccessFile arq = new RandomAccessFile("arquivo.txt", "rw");
        
        boolean apagou = false;
        arq.seek(0); //posiciona o ponteiro no inicio do arquivo = cabecalho
        int cabecalho = arq.readInt(); //descobre qual e o ultimo registro do arquivo 
        int pos = 4; //posicao do primeiro registro
        int tam = 0;
        Movies temp = new Movies();

        int aux = 0;

        do{
            arq.seek(pos); //posiciona o ponteiro no inicio do proximo registro
            boolean lapide = arq.readBoolean(); //leitura da lapide
            tam = arq.readInt(); //leitura do tamanho do registro
            if(lapide==true){ //verifica se o registro e valido
                byte[] arrayByte = new byte[tam]; 
                arq.read(arrayByte); //leitura do array de bytes
                temp.fromByteArray(arrayByte); //transforma o array de bytes em um objeto Movie
                if(temp.getId()==id){ //testa se e o registro procurado
                    apagou=true;
                    arq.seek(pos);
                    lapide = false;
                    arq.writeBoolean(lapide);
                    if(cabecalho==id){
                        arq.seek(0);
                        arq.writeInt(aux);
                    }
                    System.out.println("Registro apagado com sucesso!");
                }else{
                    aux = temp.getId();
                }
            }
            pos += tam + 4 + 1; //adiciona a quantidade de bytes para chegar no inicio do proximo registro
        }while(temp.getId()!=cabecalho && !apagou);  
        if(!apagou){
            System.out.println("Registro não encontrado!");
        }
   
        return apagou;
    }

    public static boolean update(Movies novo) throws IOException{ 
        RandomAccessFile arq = new RandomAccessFile("arquivo.txt", "rw");
        
        boolean achou = false;
        arq.seek(0); //posiciona o ponteiro no inicio do arquivo = cabecalho
        int cabecalho = arq.readInt(); //descobre qual e o ultimo registro do arquivo 
        int pos = 4; //posicao do primeiro registro
        int tam = 0;
        Movies temp = new Movies();

        do{
            arq.seek(pos); //posiciona o ponteiro no inicio do proximo registro
            boolean lapide = arq.readBoolean(); //leitura da lapide
            tam = arq.readInt(); //leitura do tamanho do registro
            if(lapide==true){ //verifica se o registro e valido
                byte[] arrayByte = new byte[tam]; 
                arq.read(arrayByte); //leitura do array de bytes
                temp.fromByteArray(arrayByte); //transforma o array de bytes em um objeto Movite
                if(temp.getId()==novo.getId()){ //testa se e o registro procurado
                    achou=true;
                    String dataFormatada = df.format(temp.getDate());
                    System.out.println("Registro encontrado! Dados atuais do registro: ");
                    System.out.println(temp.getId() + " " + dataFormatada + " " + temp.getLanguage()+ " " + temp.getTitle() + " " + temp.getAverage() + " " + temp.getGender());
                    dataFormatada = df.format(novo.getDate());
                    System.out.println("Novos dados do registro: ");
                    System.out.println(novo.getId() + " " + dataFormatada + " " + novo.getLanguage()+ " " + novo.getTitle() + " " + novo.getAverage() + " " + novo.getGender()); 
                    
                    byte[] arrayByteNovo = novo.toByteArray(); //cria-se o array de bytes com os novos campos
                    if(arrayByteNovo.length==tam){ //se os dois objetos possuirem o mesmo tamanho
                        writeArqPos(novo, pos); //escreve por cima do antigo resgistro 
                    }else{ //se possuirem tamanhos diferentes
                        arq.seek(pos);
                        lapide = false;
                        arq.writeBoolean(lapide); //apaga o registro antigo
                        writeArq(novo, "arquivo.txt"); //cria-se um novo registro no final do arquvio com o mesmo id
                    }
                    System.out.println("Registro alterado com sucesso!");
                }
            }
            pos += tam + 4 + 1; //adiciona a quantidade de bytes para chegar no inicio do proximo registro
        }while(temp.getId()!=cabecalho && !achou);  
        if(!achou){
            System.out.println("ID informado inválido!");
        }
        return achou;
    }

    public static void intercalacaoBalanceadaComum() throws IOException{
        RandomAccessFile arq = new RandomAccessFile("arquivo.txt", "rw");

        //DISTRIBUIÇAO
        arq.seek(0); //posiciona o ponteiro no inicio do arquivo = cabecalho
        int cabecalho = arq.readInt(); //descobre qual e o ultimo registro do arquivo 
        int pos = 4; //posicao do primeiro registro
        int tam = 0;
        Movies[] array = new Movies[500];
        boolean acabou = false;

        for(int i=0; i<500; i++){ //instanciar cada posição do array de Movies
            array[i] = new Movies();
        }

        int contArq = 0; //define o arquivo que receberá o array de registros
        int contArray = 0; //contabiliza os elementos do array

        do{
            //Primeira parte - preenche o array
            if(contArray<500 && !acabou){
                arq.seek(pos); //posiciona o ponteiro no inicio do proximo registro
                boolean lapide = arq.readBoolean(); //leitura da lapide
                tam = arq.readInt(); //leitura do tamanho do registro
                if(lapide==true){ //verifica se o registro e valido
                    byte[] arrayByte = new byte[tam]; 
                    arq.read(arrayByte); //leitura do array de bytes
                    array[contArray].fromByteArray(arrayByte); //transforma o array de bytes em um objeto Movie  
                    
                    //System.out.println(contArray +" "+ array[contArray].getId());
                    
                    if(array[contArray].getId()==cabecalho){ //testa se o arquivo de leitura chegou no fim
                        acabou = true;
                    }
                    contArray++; //acrescenta 1 para acompanhar a proxima posiçao
                }
                pos += tam + 4 + 1; //adiciona a quantidade de bytes para chegar no inicio do proximo registro
            

            //Segunda parte - Array ja esta preenchido, então será transferido para o proximo arquivo
            }else{
                array = quicksort(array, 0, contArray-1); //ordenação interna
                /*for(int i=0; i< array.length; i++){
                    System.out.println(array[i].getId() + " " + array[i].getTitle());
                }*/
                if(contArq%2==0){ //se o contador de arquivo for par, o vetor sera inserido no primeiro arquivo
                    for(int i=0; i<contArray; i++){
                        writeArq(array[i], "arquivo1.txt");
                    } 
                }else{ //caso contrario, o vetor sera inserido no segundo arquivo 
                    for(int i=0; i<contArray; i++){
                        writeArq(array[i], "arquivo2.txt");
                    }   
                }
                //essa parte so é importante quando o vetor não possui 500 elementos, isso é, quando o arquivo lido chego no fim
                if(contArray==500){ 
                    contArq++; //troca o arquivo que ira receber o proximo array
                    contArray=0; //para o array ser preenchido novamente, é necessario zerar o contador
                }else{ //se o vetor possuir menos que 500 elementos, significa que a distribuiçao chegou ao fim
                    contArray = -1;
                }
                
            }
    }while(!acabou || contArray>=0); 

    //FIM DA DISTRIBUIÇAO

    //readArq("arquivo1.txt");


    //INTERCALAÇAO
    
    RandomAccessFile arq1 = new RandomAccessFile("arquivo1.txt", "rw");
    RandomAccessFile arq2 = new RandomAccessFile("arquivo2.txt", "rw");
    RandomAccessFile arq3 = new RandomAccessFile("arquivo1.txt", "rw");
    RandomAccessFile arq4 = new RandomAccessFile("arquivo2.txt", "rw");

    tam = 1; //controla a quantidade de registro por bloco
    int pos1 = 4, pos2 = 4;
    Movies movie1 = new Movies();
    Movies movie2 = new Movies();

    int qtdRegistro = 10000;
    int qtdBlocos;// quantidade de blocos = qtd total de registros/(1000*tam)
    int cont=0;
    int contArqLeitura = 0; //define os arquivos que serao lidos
    contArq = 0; //define qual arquivo escrever

    float result = qtdRegistro/(1000*tam);
    qtdBlocos = (int)Math.ceil(result);
    do{
        if(contArqLeitura%2==0){ //quando os registros serão lidos dos arquivos 1 e 2
            do{
                for(int i=0; i<tam*1000; i++){ //1000 = 500 de cada bloco/registro (500+500)
                    //se o ponteiro chegar no final de algum arquivo lido, é necessario interromper a leitura deste
                    /*movie1 = readPos(pos1, "arquivo1.txt");
                    movie2 = readPos(pos2, "arquivo2.txt");
                    /*if(contArq%2==0){
                        if(movie1.getId()>movie2.getId()){
                            System.out.println(movie2.getId());
                            writeArq(movie2, "arquivo3.txt");
                            byte[] arrayB = movie2.toByteArray();
                            pos2 = 4 + 1 + arrayB.length;
                        }else if(movie1.getId()<movie2.getId()){
                            System.out.println(movie1.getId());
                            writeArq(movie1, "arquivo3.txt");
                            byte[] arrayB = movie1.toByteArray();
                            pos1 = 4 + 1 + arrayB.length;
                        }
                    }else{
                        if(movie1.getId()>movie2.getId()){
                            System.out.println(movie2.getId());
                            writeArq(movie2, "arquivo4.txt");
                            byte[] arrayB = movie2.toByteArray();
                            pos2 = 4 + 1 + arrayB.length;
                        }else if(movie1.getId()<movie2.getId()){
                            System.out.println(movie1.getId());
                            writeArq(movie1, "arquivo4.txt");
                            byte[] arrayB = movie1.toByteArray();
                            pos1 = 4 + 1 + arrayB.length;
                        }
                    }*/
                }
                contArq++; //define em qual registro escrever
                cont++; //aux para saber se chegou ao fim dos arquivos lidos
                //System.out.println("Contador: "+cont);
            }while(cont<qtdBlocos);
            cont=0;
        }else{
            /*do{
                for(int i=0; i<tam*1000; i++){ //quando os registros serão lidos dos arquivos 3 e 4
                    movie1 = readPos(pos1, "arquivo3.txt");
                    movie2 = readPos(pos2, "arquivo4.txt");
                    if(cont%2==0){
                        if(movie1.getId()>movie2.getId()){
                            writeArq(movie2, "arquivo1.txt");
                            byte[] arrayB = movie2.toByteArray();
                            pos2 = 4 + 1 + arrayB.length;
                        }else if(movie1.getId()<movie2.getId()){
                            writeArq(movie1, "arquivo1.txt");
                            byte[] arrayB = movie1.toByteArray();
                            pos1 = 4 + 1 + arrayB.length;
                        }
                    }else{
                        if(movie1.getId()>movie2.getId()){
                            writeArq(movie2, "arquivo2.txt");
                            byte[] arrayB = movie2.toByteArray();
                            pos2 = 4 + 1 + arrayB.length;
                        }else if(movie1.getId()<movie2.getId()){
                            writeArq(movie1, "arquivo2.txt");
                            byte[] arrayB = movie1.toByteArray();
                            pos1 = 4 + 1 + arrayB.length;
                        }
                    }
                }
                contArq++; //define em qual registro escrever
                cont++; //aux para saber se chegou ao fim
            }while(cont<qtdBlocos);*/
            cont=0;
        }
        tam++;
        result = (float) qtdBlocos/2;
        if(result==0.5){ //não permite loop infitino, para quando a quantidade de blocos existentes for 1
            break;
        }else{
            qtdBlocos = (int)Math.ceil(result);
        }
        contArqLeitura++; //altera os arquivos que serão lidos
        //System.out.println(result+" "+qtdBlocos);
    }while(qtdBlocos>=1); //ESSE TESTE TA ERRADO 
}

    public static Movies[] quicksort(Movies[] array, int esq, int dir){
        int i = esq, j = dir;
        Movies pivo = array[(dir+esq)/2];
        while (i <= j) {
            while (array[i].getId() < pivo.getId()) i++;
            while (array[j].getId() > pivo.getId()) j--;
            if (i <= j) {
                Movies temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        }
        if (esq < j)  quicksort(array, esq, j);
        if (i < dir)  quicksort(array, i, dir);

        return array;
    }
}
