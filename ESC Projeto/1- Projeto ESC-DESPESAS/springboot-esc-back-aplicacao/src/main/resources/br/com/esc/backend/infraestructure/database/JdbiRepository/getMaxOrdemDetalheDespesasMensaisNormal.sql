SELECT
    ISNULL(MAX(id_Ordem),0) + 1
FROM
    tbd_DetalheDespesasMensais
WHERE
    id_Despesa = :idDespesa
    AND id_DetalheDespesa = :idDetalheDespesa
    AND id_Funcionario = :idFuncionario
    AND id_DespesaParcelada = 0
    AND id_Consolidacao = 0
    AND id_DespesaConsolidacao = 0

-- Consulta realizada para inclusão de detalhes na despesa sem ser parcelada.