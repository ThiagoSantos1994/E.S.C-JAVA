SELECT
    COUNT(ds_NomeDespesa)
FROM
    tbd_DespesaMensal
WHERE
    id_Despesa <> :idDespesa
    AND id_DetalheDespesa <> :idDetalheDespesa
    AND id_Funcionario = :idFuncionario
    AND UPPER(ds_NomeDespesa) = UPPER(:dsTituloDespesa)
    AND id_Despesa IN (SELECT DISTINCT(id_Despesa) from tbd_DespesasFixasMensais WHERE id_Funcionario = :idFuncionario AND ds_Ano = :anoReferencia)