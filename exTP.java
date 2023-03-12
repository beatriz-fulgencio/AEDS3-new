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

    public static void main(String[] args) throws Exception {
        Crud byteFileRandler = new Crud("movies.db");
        Scanner sc = new Scanner(System.in);
        // carga da base de dados selecionada
        read(byteFileRandler);

        // // Ler um registro (id)
        // System.out.println("Digite o id do registro a ser procurado (com 4 dígitos, EX: 0020): ");
        // String id = sc.nextLine();
        // byteFileRandler.read(id);

        // // Atualizar um registro
        // System.out.println("Digite o id do registro a ser atualizado (com 4 dígitos, EX: 0020): ");
        // String id = sc.nextLine();
        // byteFileRandler.update(id);

        // // deletar um registro
        // System.out.println("Digite o id do registro a ser deletado (com 4 dígitos, EX: 0020): ");
        // String id = sc.nextLine();
        // byteFileRandler.delete(id);

        // criar novo registro
        // byteFileRandler.create();

        // limpar arquivo db
        // byteFileRandler.clear();

        //-----------------------------------------------------------------
        // Sort fileSort = new Sort("movies.db");

        // fileSort.clear();

        // fileSort.intercalacaoBalanceadaComum();

        // fileSort.read();

        // fileSort.intercalacaoSegmentosVariaveis();

    }

}