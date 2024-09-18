UPDATE
    tbd_Consolidacao
SET
    ds_TituloConsolidacao = :consolidacao.dsTituloConsolidacao,
    tp_Baixado = :consolidacao.tpBaixado
WHERE
    id_Consolidacao = :consolidacao.idConsolidacao
    AND id_Funcionario = :consolidacao.idFuncionario
