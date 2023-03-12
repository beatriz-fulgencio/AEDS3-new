import java.io.*;
import java.text.*;
import java.util.*;

public class exTP {

    public static int qntde = 0/* 3856 */;

    public static void read(Crud bfr) throws FileNotFoundException {
        File file = new File("netflix.csv");
        Scanner sc = null;

        int i = 0;
        try {
            sc = new Scanner(file);
            while (/* sc.hasNextLine() */ i < 3856) {
                Movie movie = new Movie(qntde++);
                movie.read(sc.nextLine());
                bfr.writeMovie(movie);
                i++;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        sc.close();

    }

    public static void aleaRead(Crud bfr) throws FileNotFoundException {
        File file = new File("netflix.csv");
        Scanner sc = null;

        int i = 0;
        try {
            sc = new Scanner(file);
            while (/* sc.hasNextLine() */ i < 3856) {
                Movie movie = new Movie((long) Math.floor(Math.random() * (3856)));
                movie.read(sc.nextLine());
                bfr.writeMovie(movie);
                i++;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        sc.close();

    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        Crud byteFileRandler = new Crud("movies.db");
        int option;
        String id;
        String tipo;

        System.out.println("Inicio------------");
        read(byteFileRandler);
        System.out.println("Base carregada com sucesso");
        System.out.println("Ver arquivos em Id.txt");
        System.out.println("Escolha uma das opções:");
        System.out.println("0 - Encerrar");
        System.out.println("1 - Carregar a base de dados");
        System.out.println("2 - Create");
        System.out.println("3 - Read Movie");
        System.out.println("4 - Update");
        System.out.println("5 - Delete");
        System.out.println("6 - Ordenar arquivo");
        System.out.println("7 - Mostrar ids ordenados");
        System.out.println("8 - Clear");
        option = Integer.parseInt(sc.nextLine());

        switch (option) {
            case 1: {
                read(byteFileRandler);
                System.out.println("Base carregada");
                break;
            }
            case 2: {
                byteFileRandler.create();
                System.out.println("Registro criado");
                break;
            }
            case 3: {
                System.out.println("Digite o id do registro a ser procurado (com 4 dígitos, EX: 0020): ");
                id = sc.nextLine();
                byteFileRandler.read(id);
            }
            case 4: {
                System.out.println("Digite o id do registro a ser atualizado (com 4 dígitos, EX: 0020): ");
                id = sc.nextLine();
                byteFileRandler.update(id);
            }
            case 5: {
                System.out.println("Digite o id do registro a ser deletado (com 4 dígitos, EX: 0020): ");
                id = sc.nextLine();
                byteFileRandler.delete(id);
            }
            case 6: {
                Sort fileSort = new Sort("movies.db");
                System.out.println("Qual tipo de ordenação?\n a)Comum \nb)Com segmentos de tamanho variável ");
                tipo = sc.nextLine();
                if (tipo == "a" || tipo == "A") {
                    fileSort.intercalacaoBalanceadaComum();
                } else if (tipo == "b" || tipo == "B") {
                    fileSort.intercalacaoSegmentosVariaveis();
                } else {
                    System.out.println("Tipo inválido");
                }
            }
            case 7: {
                Sort fileSort = new Sort("movies.db");
                fileSort.read();
                System.out.println("Ids ordenados no arquivo Id.txt");
            }
            case 8: {
                Sort fileSort = new Sort("movies.db");
                fileSort.clear();
            }
        
        }

    }

}