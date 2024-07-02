SELECT
    id_DetalheDespesa,
    ds_NomeDespesa
FROM
    tbd_DespesaMensal
WHERE
    id_Despesa = :idDespesa
    AND tp_Relatorio = 'S'
    AND id_Funcionario = :idFuncionario
ORDER BY
    ds_NomeDespesa
