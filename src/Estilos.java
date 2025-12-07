import java.awt.Color;
import java.awt.Font;

/**
 * Classe centralizada para estilos e constantes da interface gráfica.
 * Facilita a manutenção e garante consistência visual em todas as telas.
 */
public class Estilos {
    
    // Cores principais
    public static final Color COR_PRIMARIA = new Color(46, 125, 50); // Verde
    public static final Color COR_SECUNDARIA = new Color(52, 73, 94);
    public static final Color COR_SUCESSO = new Color(39, 174, 96);
    public static final Color COR_PERIGO = new Color(231, 76, 60);
    public static final Color COR_AVISO = new Color(241, 196, 15);
    public static final Color COR_FUNDO = new Color(236, 240, 241);
    public static final Color COR_TEXTO = new Color(44, 62, 80);
    public static final Color COR_BRANCO = Color.WHITE;
    public static final Color COR_CINZA = new Color(136, 136, 136);
    public static final Color COR_VERDE_CLARO = new Color(144, 238, 144);
    public static final Color COR_VERDE_CLARO_HOVER = new Color(124, 218, 124);
    
    // Fontes
    public static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONTE_PEQUENA = new Font("Segoe UI", Font.PLAIN, 12);
    
    // Tamanhos padrão
    public static final int LARGURA_TELA = 900;
    public static final int ALTURA_TELA = 700;
    public static final int LARGURA_BOTAO = 200;
    public static final int ALTURA_BOTAO = 40;
    public static final int ESPACAMENTO = 15;
    
    private Estilos() {
        // Classe utilitária - não deve ser instanciada
    }
}
