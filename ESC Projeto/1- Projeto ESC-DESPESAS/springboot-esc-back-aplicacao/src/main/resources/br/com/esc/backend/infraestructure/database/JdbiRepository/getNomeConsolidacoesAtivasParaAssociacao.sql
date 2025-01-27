SELECT DISTINCT
    id_Consolidacao,
    ds_TituloConsolidacao
FROM
    tbd_Consolidacao
WHERE
    id_Funcionario = :idFuncionario
    AND id_Consolidacao IS NOT NULL
    AND tp_Baixado = 'N'
    AND id_Consolidacao IN (SELECT ISNULL(id_Consolidacao, 0) FROM tbd_DetalheDespesasMensais WHERE id_Despesa = :idDespesa AND id_DetalheDespesa = :idDetalheDespesa AND id_Consolidacao > 0)
ORDER BY
	ds_TituloConsolidacao