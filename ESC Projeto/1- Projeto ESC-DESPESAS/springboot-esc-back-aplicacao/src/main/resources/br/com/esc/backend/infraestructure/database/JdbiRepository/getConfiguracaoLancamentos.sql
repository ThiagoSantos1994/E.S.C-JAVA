SELECT
    dt_ViradaMes,
    ds_MesReferencia,
    ds_AnoReferencia,
    tp_ViradaAutomatica,
    id_Funcionario,
    (select max(id_Acesso) from tbd_AuditoriaAcesso where id_Funcionario = :idFuncionario) as qt_Acessos
FROM
	tbd_ConfiguracoesLancamentos
WHERE
    id_Funcionario = :idFuncionario