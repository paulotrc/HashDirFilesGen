package br.paulotrc.hash;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This Command Line Program Execute a Recursively search to find files and Generate his HASH and print in console
 * Form example: java -jar HashDirFilesGen.jar 'MD5' 'directory1' 'directory2' ...
 * @param args First argument must be the Message Digest Code, the others args, paths to search files and print his hashes
 * @author Paulo de Tarso Rolim Carneiro
 * */
public class HashGen {

    protected static String MESSAGE_DIGEST_TO_HASH = null;

    /**
     * This is method main
     * @param args First argument must be the Message Digest Code, the others args, paths to search files and print his hashes
     * @author Paulo de Tarso Rolim Carneiro
     * */
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        if(args.length > 0){
            MESSAGE_DIGEST_TO_HASH = args[0];
            if(MESSAGE_DIGEST_TO_HASH != null){
                for (String path: args) {
                    if(path.contains("/") || path.contains("\\")){
                        findAndPrintPathFilesAndHash(path);
                    }
                }
            }
        }
    }

    private static String getStringHash(byte[] bytes) throws NoSuchAlgorithmException, IOException {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
            int parteBaixa = bytes[i] & 0xf;
            if (parteAlta == 0) s.append('0');
            s.append(Integer.toHexString(parteAlta | parteBaixa));
        }
        return s.toString();
    }

    private static void findAndPrintPathFilesAndHash(String caminho) throws IOException, NoSuchAlgorithmException {
        File diretorio = new File(caminho);
        for (String nome: diretorio.list()) {
            File fileDirectory = new File(diretorio + (caminho.contains("/")?"/":"\\") + nome);
            if(fileDirectory.isDirectory()){
                findAndPrintPathFilesAndHash(fileDirectory.getAbsolutePath());
            }else if(fileDirectory.isFile()){
                byte[] fileBytes = generateHashFromFile(fileDirectory);
                System.out.println("File : [ " + fileDirectory.getAbsolutePath()+" ]");
                System.out.println("Hash: [ " + getStringHash(fileBytes)+" ]");
                System.out.println("===============================================================================");
            }
        }
    }

    private static byte[] generateHashFromFile(File arquivo) throws IOException, NoSuchAlgorithmException {
        byte[] bytes = Files.readAllBytes(arquivo.toPath());
        MessageDigest md = MessageDigest.getInstance(MESSAGE_DIGEST_TO_HASH);
        md.update(bytes);
        byte[] hashMd5 = md.digest();
        return hashMd5;
    }

}