# Interface Gráfica - Sistema de Cadastro de Monitores

## Estrutura Criada

### Classes Base
- **main.java.br.com.monitoria.Estilos.java**: Centraliza todos os estilos, cores e fontes
- **main.java.br.com.monitoria.SessaoUsuario.java**: Gerencia a sessão do usuário logado (singleton)
- **main.java.br.com.monitoria.TelaBase.java**: Classe base para todas as telas com métodos utilitários

### Telas Implementadas
- **main.java.br.com.monitoria.TelaLogin.java**: Tela de login com botão verde claro
- **main.java.br.com.monitoria.TelaCadastroCoordenador.java**: Tela de cadastro do primeiro coordenador
- **main.java.br.com.monitoria.TelaPrincipal.java**: Tela principal com funcionalidades baseadas no perfil

### Inicializador
- **main.java.br.com.monitoria.InicializadorGUI.java**: Inicializa a interface gráfica

## Refatoração Recente
*   **Herança de Dependências:** Os atributos `main.java.br.com.monitoria.CentralDeInformacoes`, `main.java.br.com.monitoria.Persistencia` e `nomeArquivo`, que são comuns a várias telas, foram movidos para a classe `main.java.br.com.monitoria.TelaBase`.
*   **Injeção de Dependência:** As classes filhas (`main.java.br.com.monitoria.TelaLogin`, `main.java.br.com.monitoria.TelaCadastroCoordenador`, `main.java.br.com.monitoria.TelaPrincipal`, etc.) agora recebem essas dependências em seus construtores e as repassam para a `main.java.br.com.monitoria.TelaBase` através de `super()`.
*   **Acesso via Getters:** O acesso a esses objetos compartilhados agora é feito de forma segura através de métodos `getters` (`getCentral()`, `getPersistencia()`), melhorando o encapsulamento e a manutenibilidade do código.

## Como Usar

### 1. Adicionar Logo
Coloque o arquivo `logo.png` na raiz do projeto (mesma pasta onde está o `src`).

### 2. Inicializar a GUI
No seu `main.java.br.com.monitoria.Main.java`, substitua ou adicione:

```java
import interfaces.swing.main.java.br.com.monitoria.InicializadorGUI;

public class main.java.br.com.monitoria.Main {
    public static void main(String[] args) {
        // ... seu código existente para carregar central e persistencia ...

        // Inicializa a interface gráfica
        main.java.br.com.monitoria.InicializadorGUI.iniciar(central, persistencia, nomeArquivo);
    }
}
```

## Funcionalidades

### Tela de Login
- Logo centralizado acima do título
- Campos de email e senha
- Botão "Entrar" em verde claro
- Botão "Cadastrar main.java.br.com.monitoria.Coordenador" (aparece apenas se não houver coordenador)
- Validação de credenciais
- Redirecionamento automático baseado no perfil

### Tela de Cadastro de main.java.br.com.monitoria.Coordenador
- Logo centralizado acima do título
- Campos: Nome, E-mail, Senha, Confirmar Senha
- Validação de dados
- Aparece automaticamente se não houver coordenador cadastrado

### Tela Principal
- Cabeçalho com informações do usuário
- Botões habilitados/desabilitados baseados no perfil
- main.java.br.com.monitoria.Coordenador: acesso a todas as funcionalidades
- main.java.br.com.monitoria.Aluno: acesso apenas às funcionalidades permitidas

## Personalização

### Cores
Edite `main.java.br.com.monitoria.Estilos.java` para alterar as cores:
- `COR_VERDE_CLARO`: Cor do botão de login
- `COR_PRIMARIA`: Cor principal dos botões
- `COR_SECUNDARIA`: Cor secundária

### Logo
- Nome do arquivo: `logo.png`
- Localização: raiz do projeto
- Tamanho recomendado: 150x150 pixels (será redimensionado automaticamente)

## Próximas Telas

Para criar novas telas, estenda `main.java.br.com.monitoria.TelaBase`:

```java
import main.java.br.com.monitoria.TelaBase;

public class MinhaTela extends TelaBase {
    public MinhaTela() {
        super("Título da Tela");
    }

    @Override
    protected void criarComponentes() {
        // Implemente seus componentes aqui
    }
}
```

