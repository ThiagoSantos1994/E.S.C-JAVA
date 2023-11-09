UPDATE
    tbd_DetalheDespesasMensais
SET
    tp_VisualizacaoTemp = 'S'
WHERE
    id_Despesa = :idDespesa
    AND id_Funcionario = :idFuncionario