SELECT
	id_Lembrete,
	ds_TituloLembrete,
	chkHabilitarNotificacaoDiaria,
	tp_Segunda,
	tp_Terca,
	tp_Quarta,
	tp_Quinta,
	tp_Sexta,
	tp_Sabado,
	tp_Domingo,
	ds_DataInicial,
	ds_Observacoes,
	id_Funcionario,
	tp_Baixado,
	tp_LembreteContagemRegressiva,
	data1,
	data2,
	data3,
	data4,
	data5,
	tp_LembreteDatado,
	tp_RenovarAuto,
	nr_NumeroDias
FROM
	tbd_CadastroLembretes
WHERE
	(<whereSemanal> AND ds_DataInicial <= CONVERT (date, GETDATE()) AND tp_LembreteDatado = 'N' AND tp_Baixado = 'N' AND id_Funcionario = :idFuncionario)
    OR
    (
        tp_Segunda = 'N' AND tp_Terca = 'N' AND tp_Quarta = 'N' AND tp_Quinta = 'N'
        AND tp_Sexta = 'N' AND tp_Sabado = 'N' AND tp_Domingo = 'N'
        AND ( ds_DataInicial <= CONVERT (date, GETDATE()) OR tp_LembreteContagemRegressiva = 'S') AND tp_LembreteDatado = 'N'
        AND tp_Baixado = :tpBaixado AND id_Funcionario = :idFuncionario
    )
    OR
    (
        (data1 = CONVERT (date, GETDATE()) OR data2 = CONVERT (date, GETDATE()) OR data3 = CONVERT (date, GETDATE())
        OR data4 = CONVERT (date, GETDATE()) OR data5 = CONVERT (date, GETDATE()))
        AND tp_Baixado = :tpBaixado AND id_Funcionario = :idFuncionario
    )
ORDER BY id_Lembrete ASC