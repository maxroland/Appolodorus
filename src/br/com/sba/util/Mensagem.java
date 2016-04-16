package br.com.sba.util;

import br.com.sba.util.Dialogo.Resposta;

/**
 * Cria��o de mensagem apartir do classe de dialogo
 */
public class Mensagem {

    private Mensagem() {
    }

    public static void info(String mensagem) {
        Dialogo.mensagens("INFO", "Informa��o", mensagem);
    }

    public static void info(String mensagem, String titulo) {
        Dialogo.mensagens("INFO", titulo, mensagem);
    }

    public static void erro(String mensagem) {
        Dialogo.mensagens("ERRO", "Erro", mensagem);
    }

    public static void erro(String mensagem, String titulo) {
        Dialogo.mensagens("ERRO", titulo, mensagem);
    }

    public static void alerta(String mensagem) {
        Dialogo.mensagens("ALERTA", "Alerta", mensagem);
    }

    public static void alerta(String mensagem, String titulo) {
        Dialogo.mensagens("ALERTA", titulo, mensagem);
    }

    public static Resposta confirmar(String mensagem) {
        return Dialogo.mensageConfirmar("Confirmar", mensagem);
    }

    public static Resposta confirmar(String titulo, String mensagem) {
        return Dialogo.mensageConfirmar(titulo, mensagem);
    }
}
