SELECT
    id_DetalheDespesa
FROM
    tbd_DespesaMensal
WHERE
    id_Despesa = :idDespesa
    AND id_Funcionario = :idFuncionario
    AND tp_DebitoCartao = 'S'