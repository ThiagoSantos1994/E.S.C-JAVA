SELECT DISTINCT
    ds_NomeDespesa
FROM
    tbd_DespesaMensal
WHERE
    tp_LinhaSeparacao IS NULL
    AND ds_NomeDespesa <> '*EMP'
    AND id_Funcionario = :idFuncionario