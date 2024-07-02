UPDATE
    tbd_DetalheDespesasMensais
SET
    id_DespesaLinkRelatorio = 0,
    tp_Relatorio = 'N'
WHERE
    id_Despesa = :idDespesa
    AND id_DespesaLinkRelatorio = :idDespesaLinkRelatorio
    AND id_Funcionario = :idFuncionario