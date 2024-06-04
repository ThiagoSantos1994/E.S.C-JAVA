UPDATE
    tbd_CadastroLembretes
SET
    ds_TituloLembrete = :dsTituloLembrete,
    chkHabilitarNotificacaoDiaria = :tpHabilitaNotificacaoDiaria,
    tp_Segunda = :tpSegunda,
    tp_Terca = :tpTerca,
    tp_Quarta = :tpQuarta,
    tp_Quinta = :tpQuinta,
    tp_Sexta = :tpSexta,
    tp_Sabado = :tpSabado,
    tp_Domingo = :tpDomingo,
    ds_DataInicial = :dataInicial,
    ds_Observacoes = :dsObservacoes,
    tp_Baixado = :tpBaixado,
    tp_LembreteContagemRegressiva = :tpContagemRegressiva,
    data1 = :data1,
    data2 = :data2,
    data3 = :data3,
    data4 = :data4,
    data5 = :data5,
    tp_LembreteDatado = :tpLembreteDatado,
    tp_RenovarAuto = :tpRenovaAuto,
    nr_NumeroDias = :numeroDias
WHERE
    id_Lembrete = :idLembrete
    AND id_Funcionario = :idFuncionario
