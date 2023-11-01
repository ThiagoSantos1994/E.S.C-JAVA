DELETE FROM
    tbd_DetalheDespesasMensais
WHERE
    tp_VisualizacaoTemp = 'S'
    AND id_Funcionario = :idFuncionario