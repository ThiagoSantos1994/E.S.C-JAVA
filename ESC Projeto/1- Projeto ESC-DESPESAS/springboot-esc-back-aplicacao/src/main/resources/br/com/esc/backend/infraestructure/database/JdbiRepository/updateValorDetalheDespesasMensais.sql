UPDATE
    tbd_DetalheDespesasMensais
SET
    vl_Total = :detalhe.vlTotal
WHERE
    id_Despesa = :detalhe.idDespesa
    AND id_DetalheDespesa = :detalhe.idDetalheDespesa
    AND (:detalhe.dsDescricao IS NULL OR ds_Descricao = :detalhe.dsDescricao)
    AND id_Funcionario = :detalhe.idFuncionario
    AND tp_Reprocessar = :detalhe.tpReprocessar
    AND tp_Anotacao = :detalhe.tpAnotacao
    AND tp_Relatorio = :detalhe.tpRelatorio
    AND tp_LinhaSeparacao = :detalhe.tpLinhaSeparacao
    AND (:detalhe.idOrdem IS NULL OR id_Ordem = :detalhe.idOrdem)
