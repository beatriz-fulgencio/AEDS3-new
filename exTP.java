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

    public static void menu() throws Exception {

        Crud byteFileRandler = new Crud("movies.db");
        Scanner sc = new Scanner(System.in);
        int option;

        System.out.println("Inicio------------");
        System.out.println();
        read(byteFileRandler);
        System.out.println("Base carregada com sucesso");
        System.out.println();
        System.out.println("Escolha uma das opções:");
        System.out.println("0 - Encerrar");
        System.out.println("1 - Ver arquivos");
        System.out.println("2 - Create");
        System.out.println("3 - Read Movie");
        System.out.println("4 - Update");
        System.out.println("5 - Delete");
        System.out.println("6 - Ordenar arquivo");
        System.out.println("7 - Mostrar ids ordenados");
        System.out.println("8 - Clear");
        option = Integer.parseInt(sc.nextLine());

        String id;
        String tipo;

        if (option == 1) {
            byteFileRandler.read();
            System.out.println("Ver registros no arquivo IdsBase.txt");
        } else if (option == 2) {
            byteFileRandler.create();
            System.out.println("Registro criado");
        } else if (option == 3) {
            System.out.println("Digite o id do registro a ser procurado (com 4 dígitos, EX: 0020): ");
            id = sc.nextLine();
            byteFileRandler.read(id);
        } else if (option == 4) {
            System.out.println("Digite o id do registro a ser atualizado (com 4 dígitos, EX: 0020): ");
            id = sc.nextLine();
            byteFileRandler.update(id);
        } else if (option == 5) {
            System.out.println("Digite o id do registro a ser deletado (com 4 dígitos, EX: 0020): ");
            id = sc.nextLine();
            byteFileRandler.delete(id);
        } else if (option == 6) {
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
        } else if (option == 7) {
            Sort fileSort = new Sort("movies.db");
            fileSort.read();
            System.out.println("Ids ordenados no arquivo Id.txt");
        } else if (option == 8) {
            Sort fileSort = new Sort("movies.db");
            fileSort.clear();
        } else if (option == 0) {
            System.out.println("Fim -------");
        }

        if (option > 0) {
            menu();
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        menu();
    }
}