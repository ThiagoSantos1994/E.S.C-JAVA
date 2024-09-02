SELECT
	id_Consolidacao,
	ds_TituloConsolidacao,
	id_Funcionario,
	tp_Baixado,
	dt_Cadastro
FROM
	tbd_Consolidacao
WHERE
    id_Consolidacao = :idConsolidacao
    AND id_Funcionario = :idFuncionario