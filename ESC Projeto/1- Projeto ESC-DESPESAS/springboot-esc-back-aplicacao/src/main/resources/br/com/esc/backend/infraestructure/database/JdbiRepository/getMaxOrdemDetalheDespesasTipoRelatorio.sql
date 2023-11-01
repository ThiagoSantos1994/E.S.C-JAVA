SELECT
    ISNULL(MAX(id_Ordem),0) + 1
FROM
    tbd_DetalheDespesasMensais
WHERE
    id_Despesa = :idDespesa
    AND id_Funcionario = :idFuncionario
    AND id_DetalheDespesa = 111
    AND id_DespesaLinkRelatorio = 0