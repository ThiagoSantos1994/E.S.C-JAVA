SELECT DISTINCT
    id_DetalheDespesa
FROM
    tbd_DespesaMensal
WHERE
    UPPER(ds_NomeDespesa) = UPPER(:dsNomeDespesa)
    AND id_Funcionario = :idFuncionario