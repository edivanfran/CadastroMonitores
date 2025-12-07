package excecoes;

public class TentativaInvalidaReduzirVagasException extends RuntimeException {

    public TentativaInvalidaReduzirVagasException(String message) {
        super(message);
    }

    public TentativaInvalidaReduzirVagasException(){
        super("Tentativa de reduzir vagas para uma quantidade inválida.")
    }

}
