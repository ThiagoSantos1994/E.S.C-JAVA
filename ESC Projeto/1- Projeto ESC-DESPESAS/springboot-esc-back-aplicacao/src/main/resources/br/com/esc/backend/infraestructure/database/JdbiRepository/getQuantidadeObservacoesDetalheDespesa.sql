SELECT
    COUNT(id_Observacao)
FROM
    tbd_ObservacaoDetalheDespesaMensal
WHERE
    id_Despesa = :idDespesa
    AND id_DetalheDespesa = :idDetalheDespesa
    AND id_Observacao = :idObservacao
    AND id_Funcionario = :idFuncionario