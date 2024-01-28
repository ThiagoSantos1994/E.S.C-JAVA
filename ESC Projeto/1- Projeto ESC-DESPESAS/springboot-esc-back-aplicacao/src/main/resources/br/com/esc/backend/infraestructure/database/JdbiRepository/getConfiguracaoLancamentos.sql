SELECT
    dt_ViradaMes,
    ds_MesReferencia,
    tp_ViradaAutomatica,
    id_Funcionario
FROM
	tbd_ConfiguracoesLancamentos
WHERE
    id_Funcionario = :idFuncionario