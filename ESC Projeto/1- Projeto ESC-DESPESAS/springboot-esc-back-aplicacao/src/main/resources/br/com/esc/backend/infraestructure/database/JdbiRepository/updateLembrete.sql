UPDATE
    tbd_CadastroLembretes
SET
    ds_TituloLembrete = :lembrete.dsTituloLembrete,
    chkHabilitarNotificacaoDiaria = :lembrete.tpHabilitaNotificacaoDiaria,
    tp_Segunda = :lembrete.tpSegunda,
    tp_Terca = :lembrete.tpTerca,
    tp_Quarta = :lembrete.tpQuarta,
    tp_Quinta = :lembrete.tpQuinta,
    tp_Sexta = :lembrete.tpSexta,
    tp_Sabado = :lembrete.tpSabado,
    tp_Domingo = :lembrete.tpDomingo,
    ds_DataInicial = :lembrete.dataInicial,
    ds_Observacoes = :lembrete.dsObservacoes,
    tp_Baixado = :lembrete.tpBaixado,
    tp_LembreteContagemRegressiva = :lembrete.tpContagemRegressiva,
    data1 = :lembrete.data1,
    data2 = :lembrete.data2,
    data3 = :lembrete.data3,
    data4 = :lembrete.data4,
    data5 = :lembrete.data5,
    tp_LembreteDatado = :lembrete.tpLembreteDatado,
    tp_RenovarAuto = :lembrete.tpRenovarAuto,
    nr_NumeroDias = :lembrete.numeroDias
WHERE
    id_Lembrete = :lembrete.idLembrete
    AND id_Funcionario = :lembrete.idFuncionario
