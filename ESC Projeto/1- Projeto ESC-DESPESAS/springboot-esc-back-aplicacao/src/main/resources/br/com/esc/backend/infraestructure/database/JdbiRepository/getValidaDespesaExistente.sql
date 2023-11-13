SELECT
    id_Despesa
FROM
    tbd_DespesaMensal
WHERE
    id_Despesa = :idDespesa
    AND id_DetalheDespesa = :idDetalheDespesa
    AND id_Funcionario = :idFuncionario