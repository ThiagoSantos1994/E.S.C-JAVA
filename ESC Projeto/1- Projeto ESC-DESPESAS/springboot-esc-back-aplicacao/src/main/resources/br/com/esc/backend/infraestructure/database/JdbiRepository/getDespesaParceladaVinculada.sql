SELECT TOP 1
    a.ds_NomeDespesa
FROM
    tbd_DespesaMensal a
    INNER JOIN tbd_DetalheDespesasMensais b ON a.id_DetalheDespesa = b.id_DetalheDespesa
WHERE
    b.id_DespesaParcelada = :idDespesaParcelada
    AND a.id_Funcionario = :idFuncionario
ORDER BY 1 DESC