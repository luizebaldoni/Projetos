package com.example.trabalho_final;

import com.example.trabalho_final.Auxiliares.*;
import com.example.trabalho_final.Auxiliares.Investimentos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainController {
    @FXML
    private TextField limiteField;
    @FXML
    private ListView<Financeiro> listView;
    @FXML
    private TextField descricaoField;
    @FXML
    private TextField montanteField;
    @FXML
    private ComboBox<String> tipoComboBox;
    @FXML
    private Label saldoLabel;

    private final ObservableList<Financeiro> registro = FXCollections.observableArrayList();

    private double limiteGastos = 0.0; // Atributo para armazenar o limite de gastos

    //// INICIALIZANDO O CONTROLLER ////
    @FXML
    public void initialize() {
        listView.setItems(registro);
        tipoComboBox.getItems().setAll("Montante", "Despesa", "Receita Fixa", "Investimentos", "Empréstimos", "Metas de Poupança, Impostos");
        atualizaSaldo();
    }
    //// FIM INICIALIZANDO O CONTROLLER ////

    //// VALIDANDO ENTRADAS ////
    //~ validando descrição
    private void validarDescricao(String descricao) {
        if (descricao.isEmpty()) {
            throw new Excecao_descricao("Descrição não pode estar vazia!");
        }
    }

    //~ validando montante
    private double validarMontante(String montanteText) {
        if (montanteText.isEmpty()) {
            throw new Excecao_montante("Montante não pode estar vazio!");
        }
        try {
            double montante = Double.parseDouble(montanteText);
            if (montante <= 0) {
                throw new Excecao_montante("Montante deve ser maior que zero.");
            }
            return montante;
        }
        catch (NumberFormatException ex) {
            throw new Excecao_montante("Montante deve ser um número válido.");
        }
    }

    //~ validando tipo
    private void validarTipo(String tipo) {
        if (tipo == null) {
            throw new Excecao_tipo("Você deve selecionar um tipo.");
        }
    }
    //// FIM VALIDANDO ENTRADAS ////

    //// CRIANDO REGISTRO ////
    private Financeiro criarRegistro(String tipo, String descricao, double montante) {
        switch (tipo) {
            case "Montante":
                return new Montante(descricao, montante); //~ Montante

            case "Despesa":
                return new Despesas(descricao, montante); //~ Despesas

            case "Receita Fixa":
                return new ReceitaFixa(descricao, montante); //~ ReceitaFixa

            case "Investimentos":
                double taxaJuros = solicitarValorUsuario("Informe a taxa de juros do investimento (%):", 0.05); //~ Investimentos
                return new Investimentos(descricao, montante, taxaJuros / 100); //~ calculo da taxa em porcentagem

            case "Empréstimos":
                int parcelas = (int) solicitarValorUsuario("Informe o número de parcelas do empréstimo:", 12); //~ Empréstimos
                return new Emprestimos(descricao, montante, parcelas);

            case "Metas de Poupança":
                double objetivo = solicitarValorUsuario("Informe o objetivo da poupança:", 5000.00); //~ MetasPoupanca
                return new MetasPoupanca(descricao, montante, objetivo);

            case "Impostos":
                String categoria = solicitarTextoUsuario("Informe a categoria do imposto:", "Imposto de Renda"); //~ Impostos
                return new Impostos(descricao, montante, categoria);

            default:
                throw new IllegalArgumentException("Tipo de registro desconhecido."); //~ erro
        }
    }
    //// FIM CRIANDO REGISTRO ////

    //// LIMPAR CAMPOS ////
    private void limpaFields() {
        descricaoField.clear();
        montanteField.clear();
        tipoComboBox.setValue(null);
    }
    //// FIM LIMPAR CAMPOS ////


    //// AÇÕES DE CADA BOTÃO DO LAYOUT ////

    //~ ADICIONA REGISTRO
    @FXML
    private void onAddRegistro() {
        try { //~ validando e criando o registro (tenta add o registro)
            String descricao = descricaoField.getText().trim();
            validarDescricao(descricao);

            double montante = validarMontante(montanteField.getText().trim());

            String tipo = tipoComboBox.getValue(); //~ verifica se o tipo foi selecionado
            validarTipo(tipo);

            Financeiro record = criarRegistro(tipo, descricao, montante);
            registro.add(record);

            limpaFields();
            atualizaSaldo();
        }
        catch (Excecao_descricao | Excecao_montante | Excecao_tipo ex) { //~ exibe mensagem de erro caso seja um erro de validação
            showAlert("Erro de Validação!", ex.getMessage(), Alert.AlertType.WARNING);
        }
        catch (Exception ex) {
            showAlert("Erro!", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
     //~ REMOVER REGISTRO
    @FXML
    private void onRemoveRegistro() {
        Financeiro selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            registro.remove(selected);
            atualizaSaldo();
        }
        else {
            showAlert("Erro", "Nenhum registro selecionado para remover.", Alert.AlertType.WARNING);
        }
    }
    //~ GERAR RELATÓRIO
    @FXML
    private void onGerarRelatorio() {
        //~atualiza o total de valor revebido
        double totalDespesas = registro.stream().filter(record -> record instanceof Despesas).mapToDouble(Financeiro::get_montante).sum();
        //~ atualiza o total de valor gasto
        double totalReceitas = registro.stream().filter(record -> record instanceof Montante).mapToDouble(Financeiro::get_montante).sum();
        //~calcula o saldo total e mostra o valor de cada irem
        String relatorio = String.format("Total de Despesas: R$ %.2f\nTotal de Receitas: R$ %.2f\nSaldo: R$ %.2f", totalDespesas, totalReceitas, totalReceitas - totalDespesas);
        showAlert("Relatório Financeiro", relatorio, Alert.AlertType.INFORMATION); //~ exibe em forma de alerta
    }
    //~ EXPORTAR CSV
    @FXML
    private void onExportarCSV() {
        FileChooser fileChooser = new FileChooser(); //~ abre selecçao de arquivos
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(listView.getScene().getWindow());

        if (file != null) { //~ verifica se o arquivo foi selecionado
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Financeiro record : registro) { //~ percorre a lista de registros e escreve os dados no arquivo
                    writer.write(record.get_descricao() + "," + record.get_montante());
                    writer.newLine();
                }
                showAlert("Sucesso", "Dados exportados com sucesso!", Alert.AlertType.INFORMATION);
            }
            catch (IOException e) { //~ exibe mensagem de erro caso tenha dado algum problema
                showAlert("Erro", "Erro ao exportar dados: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    //~ VERIFICAR LIMITE DE GASTOS
    @FXML
    private void onVerificarLimite() {
        double limite = limiteGastos; // Usando o limite de gastos definido pelo usuário
        //~atualiza o total de valor recebido
        double totalDespesas = registro.stream().filter(record -> record instanceof Despesas).mapToDouble(Financeiro::get_montante).sum();

        if (totalDespesas > limite) { //~ verifica se o total de despesas excede o limite
            showAlert("Alerta de Limite", "Você excedeu o limite de despesas de R$ " + limite, Alert.AlertType.WARNING);
        }
        else {    //~ exibe mensagem de sucesso caso nao tenha excedido
            showAlert("Limite", "Você está dentro do limite de despesas.", Alert.AlertType.INFORMATION);
        }
    }

    //~ DEFINIR LIMITE DE GASTOS
    @FXML
    private void onDefinirLimite() {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(limiteGastos));
        dialog.setTitle("Definir Limite de Gastos");
        dialog.setHeaderText(null);
        dialog.setContentText("Insira o limite de gastos desejado:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                limiteGastos = Double.parseDouble(result.get().trim());
                showAlert("Limite Definido", "Seu limite de gastos foi definido como: R$ " + limiteGastos, Alert.AlertType.INFORMATION);
            } catch (NumberFormatException e) {
                showAlert("Erro", "Por favor, insira um valor numérico válido para o limite.", Alert.AlertType.ERROR);
            }
        }
    }

    //~ SALVAR REGISTRO
    @FXML
    private void onSalvarRegistro() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Registros");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos de Texto", "*.txt"));
        File file = fileChooser.showSaveDialog(listView.getScene().getWindow());

        if (file != null) { //~ verifica se o arquivo foi selecionado
            try {
                salvarRegistroToFile(file.getAbsolutePath()); //~ salva o arquivo
                showAlert("Sucesso", "O arquivo foi salvo com sucesso.", Alert.AlertType.INFORMATION);
            }
            catch (IOException ex) { //~ exibe mensagem de erro caso nao tenha conseguido salvar o arquivo
                showAlert("Erro", "Não foi possível salvar o arquivo: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    //~ CARREGAR REGISTRO
    @FXML
    private void onCarregarRegistro() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Carregar Registros");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos de Texto", "*.txt"));
        File file = fileChooser.showOpenDialog(listView.getScene().getWindow());

        if (file != null) { //~ verifica se o arquivo foi selecionado
            try {
                carregarRegistroFromFile(file.getAbsolutePath());
                showAlert("Sucesso", "Registros carregados com sucesso.", Alert.AlertType.INFORMATION);
                atualizaSaldo();
            }
            catch (FileNotFoundException ex) { //~ exibe mensagem de erro caso nao tenha encontrado o arquivo
                showAlert("Erro", "Arquivo não encontrado.", Alert.AlertType.ERROR);
            }
            catch (IOException ex) { //~ exibe mensagem de erro caso nao tenha conseguido carregar o arquivo
                showAlert("Erro", "Não foi possível carregar os registros do arquivo: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
            catch (Exception ex) { //~ mensagem caso seja um erro nao previsto
                showAlert("Erro inesperado", "Ocorreu um erro inesperado ao carregar os registros: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    ////// FIM AÇÃO DOS BOTOES ////

    //// SOLICITAÇÕES AO USUÁRIO ////
    private double solicitarValorUsuario(String mensagem, double valorPadrao) {

        TextInputDialog dialog = new TextInputDialog(String.valueOf(valorPadrao));
        dialog.setTitle("Entrada de Valor");
        dialog.setHeaderText(null);
        dialog.setContentText(mensagem);
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) { //~ verifica se o valor foi inserido
            try { //~ tenta converter o valor para double
                return Double.parseDouble(result.get().trim());
            }
            catch (NumberFormatException e) { //~ exibe mensagem de erro caso tenha dado algum problema
                throw new IllegalArgumentException("Entrada inválida. Por favor, insira um valor numérico.");
            }
        }
        else { //~ exibe mensagem de erro caso nao tenha inserido um valor
            throw new IllegalArgumentException("Operação cancelada pelo usuário.");
        }
    }
    //~ SOLICITANDO TEXTO
    private String solicitarTextoUsuario(String mensagem, String textoPadrao) {

        TextInputDialog dialog = new TextInputDialog(textoPadrao);
        dialog.setTitle("Entrada de Texto");
        dialog.setHeaderText(null);
        dialog.setContentText(mensagem);
        Optional<String> result = dialog.showAndWait();

        return result.orElseThrow(() -> new IllegalArgumentException("Operação cancelada pelo usuário.")).trim();
    }
    //// FIM SOLICITAÇÕES AO USUÁRIO ////

    /////// METODOS AUXILIARES //////

    //~ SALVANDO REGISTRO
    private void salvarRegistroToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Financeiro record : registro) {
                String tipo = (record instanceof Montante) ? "Recebido" : "Gasto";
                writer.write(tipo + "," + record.get_descricao() + "," + record.get_montante());
                writer.newLine();
            }
        }
    }
    //~ CARREGANDO REGISTRO
    private void carregarRegistroFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            List<Financeiro> loadedRegistro = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 3) continue;

                String tipo = parts[0];
                String descricao = parts[1];
                double montante = Double.parseDouble(parts[2]);

                Financeiro record = tipo.equals("Recebido") ? new Montante(descricao, montante) : new Despesas(descricao, montante);
                loadedRegistro.add(record);
            }
            registro.setAll(loadedRegistro);
        }
    }
    //~ ATUALIZAÇÃO DE SALDO
    private void atualizaSaldo() {
        //~atualiza o total de valor reebido
        double totalReceitas = registro.stream().filter(record -> record instanceof Montante).mapToDouble(Financeiro::get_montante).sum();
        //~ atualiza o total de valor gasto
        double totalDespesas = registro.stream().filter(record -> record instanceof Despesas).mapToDouble(Financeiro::get_montante).sum();
        saldoLabel.setText(String.format("R$ %.2f", totalReceitas - totalDespesas)); //~ calcula o saldo
    }

    //~ EXIBIÇÃO DE ALERTA
    private void showAlert(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
