DELETE FROM
    tbd_DespesaMensal
WHERE
   tp_VisualizacaoTemp = 'S'
   AND id_Funcionario = :idFuncionario