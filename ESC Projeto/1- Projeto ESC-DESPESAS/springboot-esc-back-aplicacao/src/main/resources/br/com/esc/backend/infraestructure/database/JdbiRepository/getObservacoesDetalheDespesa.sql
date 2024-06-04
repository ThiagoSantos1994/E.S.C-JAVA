SELECT
    ds_AnotacaoDespesa
FROM
    tbd_ObservacaoDetalheDespesaMensal
WHERE
    id_Despesa = :idDespesa
    AND id_DetalheDespesa = :idDetalheDespesa
    AND id_Ordem = :idOrdem
    AND id_Funcionario = :idFuncionario