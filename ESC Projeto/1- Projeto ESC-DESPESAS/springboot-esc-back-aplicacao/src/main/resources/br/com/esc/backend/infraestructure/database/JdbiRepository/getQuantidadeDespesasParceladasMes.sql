SELECT
    COUNT(ds_Descricao)
FROM
    tbd_DetalheDespesasMensais
WHERE
    ds_Descricao = '*PRC'
    AND id_Despesa = :idDespesa
    AND id_Funcionario = :idFuncionario
